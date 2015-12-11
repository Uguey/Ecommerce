package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.utils.Utils;

/**
 * Servlet that controls adding, removing and visualizing products from the shopping cart.
 */
@WebServlet("/shoppingcart")
public class ShoppingCartServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  private static final String ACTION_ADD = "add";
  private static final String ACTION_DELETE = "delete";
  private static final String ACTION_DECREMENT = "decrement";
  private static final String ACTION_INCREMENT = "increment";

  /**
   * Shows the current products of the current purchase by the user.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client authenticatedClient = dbhelper.findAuthenticatedClient(request);
    try {
      if (authenticatedClient != null) {
        request.setAttribute("purchaseItems", dbhelper.getShoppingCartPurchase(authenticatedClient)
            .getPurchaseItems());
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.SHOPPING_CART)
            .forward(request, response);

      } else {
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
      }
    } catch (Exception e) {
      e.printStackTrace();
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }
  }

  /**
   * Modifies the products in the shopping cart, either by adding, or removing them.
   * 
   * Returns the number of total items in the shopping cart after the operation.
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Client client = dbhelper.findAuthenticatedClient(request);
    String idS = request.getParameter(Utils.PARAM_ID);

    String action = request.getParameter(Utils.PARAM_ACTION);
    if (action == null)
      action = ACTION_ADD;

    if (client != null && idS != null) {
      boolean error = false;
      Product prod = null;
      try {
        prod = dbhelper.findProductById(idS);
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }

      if (ACTION_ADD.equals(action)) {
        error = addOneProductToCart(client, prod);
      } else if (ACTION_DELETE.equals(action)) {
        error = removeProductFromCart(client, prod);
      } else if (ACTION_INCREMENT.equals(action)) {
        error = addOneProductToCart(client, prod);
      } else if (ACTION_DECREMENT.equals(action)) {
        error = removeOneProductFromCart(client, prod);
      }

      if (error) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }
    }

    returnNumItemsInCart(client, response);
  }

  /**
   * Return a response in plain text with the number of items the client has in the cart after the
   * operation.
   */
  private void returnNumItemsInCart(Client client, HttpServletResponse response) {
    int numItems = 0;
    try {
      numItems = dbhelper.numItemsInShoppingCart(client);
    } catch (Exception e) {
      e.printStackTrace();
    }
    response.setCharacterEncoding("utf-8");
    try {
      PrintWriter out = response.getWriter();
      out.println(numItems);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds one unit of a product to the shopping cart.
   * 
   * @return if there was an error.
   */
  private boolean addOneProductToCart(Client client, Product prod) {
    if (client != null && prod != null && prod.getStock() > 0) {
      try {
        dbhelper.addToShoppingCart(client, prod, 1);
        return false;
      } catch (Exception e) {
        e.printStackTrace();
        return true;
      }
    } else
      return true;
  }

  /**
   * Removes one unit of the given product from the shopping cart
   */
  private boolean removeOneProductFromCart(Client client, Product prod) {
    try {
      if (client != null && prod != null && dbhelper.shoppingCartContainsProduct(client, prod)) {
        dbhelper.removeFromShoppingCart(client, prod, 1);
        return false;
      } else
        return true;
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }
  }

  /**
   * Removes all units of a product from the shopping cart
   */
  private boolean removeProductFromCart(Client client, Product prod) {
    try {
      if (client != null && prod != null && dbhelper.shoppingCartContainsProduct(client, prod)) {
        dbhelper.removeFromShoppingCart(client, prod, -1);
        return false;
      } else
        return true;
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }
  }
}
