package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.Purchase;
import es.uc3m.tiw.g01.model.entities.PurchaseItem;

/**
 * Servlet implementation class OrderHistoryServlet
 */
@WebServlet("/orderhistorydetails")
public class OrderHistoryDetailsServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client c = dbhelper.findAuthenticatedClient(request);

    if (c == null) {
      getServletContext().getRequestDispatcher(Routes.JSP.ERROR).forward(request, response);
      return;
    }

    Purchase purchase;
    try {
      purchase = dbhelper.findPurchaseByID(request.getParameter("pid"));
    } catch (NumberFormatException e) {
      // Id empty or NaN
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    if (purchase != null) {

      request.setAttribute("purchaseItems", purchase.getPurchaseItems());

      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ORDER_HISTORY_DETAILS)
          .forward(request, response);

    } else {
      goToError(request, response);
      return;
    }
  }

  private void goToError(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    getServletContext().getRequestDispatcher(Routes.JSP.ERROR).forward(request, response);
  }
}
