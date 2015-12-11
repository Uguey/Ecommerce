package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.Subscription;
import es.uc3m.tiw.g01.model.utils.Utils;

/**
 * Servlet implementation class addWatchListServlet
 */
@WebServlet("/watchlist")
public class WatchListServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Client client = dbhelper.findAuthenticatedClient(request);
    List<Subscription> subscriptions;
    if (client != null) {
      subscriptions = dbhelper.findSubscriptionsByClient(client);
      if (subscriptions != null) {
        request.setAttribute("subscriptions", subscriptions);
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.WATCH_LIST)
            .forward(request, response);
      }
    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client client = dbhelper.findAuthenticatedClient(request);
    String action = request.getParameter("action-subscription");

    // finding product
    String idS = request.getParameter(Utils.PARAM_ID);
    if ((client != null) && (idS != null)) {
      Product prod = null;
      try {
        prod = dbhelper.findProductById(idS);
      } catch (Exception e) {
        e.printStackTrace();
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
        return;
      }
      if (action.equalsIgnoreCase("delete")) {
        // delete subscription
        try {
          dbhelper.removeSubscriptionByClientAndProduct(client, prod);
        } catch (Exception e) {
          e.printStackTrace();
          getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
              .forward(request, response);
          return;
        }
      } else {
        // create subscription
        Subscription s = null;
        try {
          s = dbhelper.createSubscription(client, prod);
        } catch (Exception e) {
          e.printStackTrace();
          getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
              .forward(request, response);
          return;
        }
        if (s == null) {
          getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
              .forward(request, response);
          return;
        }
      }
      response.sendRedirect("product?id=" + prod.getId());

    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

  }

}
