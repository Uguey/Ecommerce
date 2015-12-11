package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import es.uc3m.tiw.g01.client.queues.OrderQueue;
import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.json.PaymentResponse;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Coupon;
import es.uc3m.tiw.g01.model.entities.Purchase;
import es.uc3m.tiw.g01.model.utils.Utils;
import es.uc3m.tiw.g01.orders.CouponManagerLocal;
import es.uc3m.tiw.g01.orders.OrderManagerLocal;

/**
 * Controller for all payment related operations.
 */
@WebServlet("/payment")
public class PaymentServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  private OrderQueue queue;

  @Override
  public void init() throws ServletException {
    super.init();
    queue = new OrderQueue();
  }

  @EJB
  private CouponManagerLocal couponManager;

  @EJB
  private OrderManagerLocal orderManager;

  /**
   * Returns the payment page
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client authenticatedClient = dbhelper.findAuthenticatedClient(request);

    try {
      if (authenticatedClient == null || dbhelper.numItemsInShoppingCart(authenticatedClient) <= 0) {
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
        return;
      } else {
        request.setAttribute("purchaseItems", dbhelper.getShoppingCartPurchase(authenticatedClient)
            .getPurchaseItems());
      }
    } catch (Exception e) {
      e.printStackTrace();
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    if (authenticatedClient.getAddress() != null) {
      String[] parts = authenticatedClient.getAddress().split("%");
      // showing blank address if spaces
      if (parts[0].equals(" "))
        parts[0] = "";
      if (parts[1].equals(" "))
        parts[1] = "";
      if (parts[2].equals(" "))
        parts[2] = "";
      request.setAttribute("address", parts[0]);
      request.setAttribute("cp", parts[1]);
      request.setAttribute("city", parts[2]);
    }

    // Set the list of coupons as an attribute, if them exist
    List<Coupon> coupons = couponManager.getRedeemableCoupons(authenticatedClient);
    if (coupons != null && !coupons.isEmpty()) {
      request.setAttribute("coupons", coupons);
    }

    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.PAYMENT)
        .forward(request, response);
  }

  /**
   * Performs the payment
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client authenticatedClient = dbhelper.findAuthenticatedClient(request);

    try {
      if ((authenticatedClient == null)
          || (dbhelper.numItemsInShoppingCart(authenticatedClient) <= 0)) {
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
        return;
      }

      // Set the value of the badge in the shopping cart icon back to be
      // empty.
      response.addCookie(new Cookie("num_items_in_cart", "0"));

    } catch (Exception e) {
      e.printStackTrace();
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    String address = request.getParameter("client-address");
    String cp = request.getParameter("client-cp");
    String country = request.getParameter("client-city");
    String accountName = request.getParameter("client-account");
    String creditCard = request.getParameter("client-creditCard");
    String expirationMonth = request.getParameter("client-expirationMonth");
    String expirationYear = request.getParameter("client-expirationYear");
    String cvv = request.getParameter("client-cvv");
    String couponId = request.getParameter("client-coupon");
    String totalPrice = request.getParameter("total-price");

    boolean validationCorrect = false;

    // payment validation
    if (!Utils.validateNotEmpty(address)) {
      request.setAttribute("validation_error", "Enter a valid address");
    } else if (!Utils.validateNumber(cp, true, true) || cp.length() != 5) {
      request.setAttribute("validation_error", "Enter a valid postal code (5 numbers)");
    } else if (!Utils.validateNotEmpty(country)) {
      request.setAttribute("validation_error", "Enter a valid city");
    } else if (!Utils.validateNotEmpty(accountName)) {
      request.setAttribute("validation_error", "Enter a valid account name");
    } else if (!Utils.validateNotEmpty(creditCard) || creditCard.length() != 20) {
      request.setAttribute("validation_error",
          "Enter a valid credit card (20 alphanumeric characters)");
    } else if (!Utils.validateNumber(expirationMonth, true, true)) {
      request.setAttribute("validation_error", "Enter a valid expiration month");
    } else if (!Utils.validateNumber(expirationYear, true, true)) {
      request.setAttribute("validation_error", "Enter a valid expiration year");
    } else if (!Utils.validateNumber(cvv, true, true) || cvv.length() != 3) {
      request.setAttribute("validation_error", "Enter a valid cvv (3 numbers)");
    } else {
      // If this else is executed, then we passed all validations
      validationCorrect = true;
    }
    if (!validationCorrect) {
      // Redirect with error
      doGet(request, response);
      return;
    }

    // save full address in client
    String shipmentAddress = address + "%" + cp + "%" + country;
    authenticatedClient.setAddress(shipmentAddress);
    try {
      dbhelper.updateClientById(authenticatedClient.getId(), authenticatedClient);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // send parameters to bank
    Coupon coupon = couponManager.getCoupon(couponId);
    BigDecimal finalPrice = new BigDecimal(totalPrice);
    String couponCode = "";
    if (coupon != null) {
      finalPrice = finalPrice.subtract(coupon.getRedeemableValue(finalPrice));
      couponCode = coupon.getCode();
    }
    String url = "http://localhost:8080/BancoWEB/payment/perform";
    javax.ws.rs.client.Client client = ClientBuilder.newClient();
    WebTarget web =
        client.target(url).queryParam("creditcard", creditCard)
            .queryParam("ordercode", orderManager.generateOrderCode())
            .queryParam("amount", finalPrice).queryParam("couponcode", couponCode);
    javax.ws.rs.client.Invocation.Builder builder = web.request(MediaType.APPLICATION_JSON);
    PaymentResponse res = builder.get(PaymentResponse.class);

    boolean bankValidation = true;
    switch (res.getResult()) {
      case PaymentResponse.RESULT_CODE_OK:
        Purchase p;
        // close purchase and save address
        try {
          // apply coupons if selected
          p = dbhelper.buyShoppingCart(authenticatedClient, shipmentAddress, coupon);

          if (p != null) {
            if (coupon != null) {
              couponManager.redeemCoupon(coupon);
            }
          } else {
            getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
                .forward(request, response);
            return;
          }

        } catch (Exception e) {

          e.printStackTrace();
          getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
              .forward(request, response);
          return;
        }

        // send purchase to administrador
        try {
          queue.enqueuePurchase(p);
        } catch (NamingException e) {
          e.printStackTrace();
        }

        // Set the final list of products bought.
        request.setAttribute("purchaseItems", p.getPurchaseItems());
        request.setAttribute("purchase", p);

        // Generate and tell the client if he got a coupon
        int couponValue = couponManager.generateCouponFor(p, authenticatedClient);

        request.setAttribute("coupon", couponValue);

        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.PAYMENT_DONE)
            .forward(request, response);
        break;

      case PaymentResponse.RESULT_CODE_INVALID_CREDIT_CARD:
        request.setAttribute("validation_error",
            "The bank declined yor credit card. Please insert a valid credit card (20 "
                + "alphanumeric characters beginning with 'A' or 'B')");
        bankValidation = false;
        break;

      case PaymentResponse.RESULT_CODE_MISSING_PARAMS:
        request.setAttribute("validation_error", "Internal Server Error, please try again");
        bankValidation = false;
        break;

      case PaymentResponse.RESULT_CODE_UNKNOWN:
        request.setAttribute("validation_error", "Internal Server Error, please try again");
        bankValidation = false;
        break;
    }
    if (!bankValidation) {
      // Redirect with error
      doGet(request, response);
      return;
    }


  }
}
