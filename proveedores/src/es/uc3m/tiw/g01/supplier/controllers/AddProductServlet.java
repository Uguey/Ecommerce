package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.Supplier;
import es.uc3m.tiw.g01.supplier.routes.Routes;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/addproduct")
@MultipartConfig(location = "/tmp")
public class AddProductServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {

    Supplier supplier = dbhelper.findAuthenticatedSupplier(req);
    if (supplier == null) {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(req, resp);
      return;
    }

    req.setAttribute("categories", dbhelper.findAllProductCategories());

    getServletContext().getRequestDispatcher(Routes.JSP.NEW_PRODUCT).forward(req, resp);
  }

  /**
   * Adds a product to the catalog of the supplier
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Supplier supplier = dbhelper.findAuthenticatedSupplier(request);
    if (supplier == null) {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    Product p = new Product();
    p = Product.parseFromRequest(request, p, dbhelper);
    if (p != null) {
      p.setSupplier(supplier);
      p.setMargin(new BigDecimal(0));
      p.setMarginIsPercentage(false);
      try {
        dbhelper.saveNewProduct(p);
      } catch (Exception e) {
        e.printStackTrace();
        getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
            .forward(request, response);
        return;
      }
    } else {
      request.setAttribute("categories", dbhelper.findAllProductCategories());
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.NEW_PRODUCT)
          .forward(request, response);
      return;
    }
    response.sendRedirect("");
  }
}
