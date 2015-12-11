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
import es.uc3m.tiw.g01.model.entities.Purchase;
import es.uc3m.tiw.g01.model.utils.DatabaseHelper;

/**
 * Servlet implementation class OrderHistoryServlet
 */
@WebServlet("/orderhistory")
public class OrderHistoryServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client authenticatedClient = dbhelper.findAuthenticatedClient(request);
    try {
      if (authenticatedClient != null) {
        request.setAttribute("client", authenticatedClient);

        String orderby = request.getParameter("orderby");
        List<Purchase> purchases = null;

        if (orderby == null) {
          purchases =
              dbhelper.getPaidPurchases(authenticatedClient, DatabaseHelper.ORDER_BY_DEFAULT);
        } else if (orderby.equalsIgnoreCase("dateAsc")) {
          purchases =
              dbhelper.getPaidPurchases(authenticatedClient, DatabaseHelper.ORDER_BY_DATE_ASC);
        } else if (orderby.equalsIgnoreCase("dateDesc")) {
          purchases =
              dbhelper.getPaidPurchases(authenticatedClient, DatabaseHelper.ORDER_BY_DATE_DESC);
        } else {
          purchases =
              dbhelper.getPaidPurchases(authenticatedClient, DatabaseHelper.ORDER_BY_DEFAULT);
        }

        request.setAttribute("purchases", purchases);

        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ORDER_HISTORY)
            .forward(request, response);
      } else {
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
      }
    } catch (Exception e) {
      e.printStackTrace();
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
    }
  }
}
