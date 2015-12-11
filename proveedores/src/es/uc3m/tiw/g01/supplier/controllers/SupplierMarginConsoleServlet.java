package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Supplier;
import es.uc3m.tiw.g01.model.utils.SalesTuple;
import es.uc3m.tiw.g01.supplier.routes.Routes;

/**
 * Servlet implementation class SupplierConsoleServlet
 */
@WebServlet("/margin")
public class SupplierMarginConsoleServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Supplier authenticatedSupplier = dbhelper.findAuthenticatedSupplier(request);

    if (authenticatedSupplier != null) {
      try {
        List<SalesTuple> sales = dbhelper.getSalesPerMonth(authenticatedSupplier);

        request.setAttribute("sales", sales);

        request.setAttribute("purchaseItems",
            dbhelper.getSplittedPurchaseItemsForSupplier(authenticatedSupplier));

        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.BILLING)
            .forward(request, response);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      response.sendRedirect("login");
    }
  }
}
