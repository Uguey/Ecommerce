package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Subscription;
import es.uc3m.tiw.g01.model.entities.Supplier;
import es.uc3m.tiw.g01.supplier.routes.Routes;

/**
 * Displays all the products the client has subscribed to.
 */
@WebServlet("/subscribedproducts")
public class SubscribedProductsServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Supplier supplier = dbhelper.findAuthenticatedSupplier(request);
    List<Subscription> subscriptions;
    if (supplier != null) {
      subscriptions = dbhelper.findSubscriptionsBySupplier(supplier);
      if (subscriptions != null) {
        request.setAttribute("subscriptions", subscriptions);
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.SUBSCRIBED_PRODUCTS)
            .forward(request, response);
      }
    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.LOGIN)
          .forward(request, response);
      return;
    }
  }

}
