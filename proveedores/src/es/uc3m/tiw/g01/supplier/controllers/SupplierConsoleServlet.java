package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Setting;
import es.uc3m.tiw.g01.model.entities.Supplier;
import es.uc3m.tiw.g01.supplier.routes.Routes;

/**
 * Servlet implementation class SupplierConsoleServlet
 */
@WebServlet("")
public class SupplierConsoleServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Supplier authenticatedSupplier = dbhelper.findAuthenticatedSupplier(request);
    Setting s = dbhelper.getSetting();

    if (authenticatedSupplier != null) {
      request.setAttribute("categories", dbhelper.findAllProductCategories());
      request.setAttribute("products", authenticatedSupplier.getProducts());
      request.setAttribute("stockThresHoldLow", s.getStockThresHoldLow());
      request.setAttribute("stockThresHoldVeryLow", s.getStockThresHoldVeryLow());

      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.CONSOLE_SUPPLIER)
          .forward(request, response);

    } else {
      response.sendRedirect("login");
    }
  }
}
