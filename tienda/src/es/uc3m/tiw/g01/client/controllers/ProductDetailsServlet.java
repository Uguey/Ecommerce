package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.Client;

import es.uc3m.tiw.g01.model.entities.Subscription;

@WebServlet("/product")
public class ProductDetailsServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Create a set of products only of the category requested, and send it to products.jsp
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String sId = request.getParameter("id");
    if (sId == null) {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    } else {
      Product p = null;
      try {
        p = dbhelper.findProductById(sId);
      } catch (Exception e) {
        e.printStackTrace();
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
        return;
      }
      // Subscription
      Client client = dbhelper.findAuthenticatedClient(request);
      Subscription s = null;
      if (client != null) {
        s = dbhelper.findSubscriptionByClientAndProduct(client.getId(), p.getId());
      }
      if (s == null) {
        request.setAttribute("subscription", false);
      } else {
        request.setAttribute("subscription", true);
      }

      if (p == null) {
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
        return;
      } else {
        request.setAttribute("product", p);
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.PRODUCT)
            .forward(request, response);
      }
    }
  }
}
