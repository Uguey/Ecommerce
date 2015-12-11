package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.ProductCategory;
import es.uc3m.tiw.g01.model.entities.Supplier;
import es.uc3m.tiw.g01.model.utils.Utils;
import es.uc3m.tiw.g01.supplier.routes.Routes;

/**
 * Servlet implementation class EditProductServlet
 */
@WebServlet("/editproduct")
public class EditProductServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Edit the product of the Supplier Console
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Supplier supplier = dbhelper.findAuthenticatedSupplier(request);
    if (supplier == null) {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    String action = request.getParameter("action");
    String idS = request.getParameter("id");

    if (idS == null || action == null) {
      renderError(response);
      return;
    }

    if (action.equals("edit")) {
      try {
        ProductCategory pc =
            dbhelper.findProductCategoryById(request.getParameter(Utils.PARAM_CATEGORY));
        Product p = dbhelper.findProductById(idS);
        p = Product.parseFromRequest(request, p, dbhelper);
        if (p != null) {
          dbhelper.updateProductById(p.getId(), request.getParameter(Utils.PARAM_NAME), request
              .getParameter(Utils.PARAM_DESCRIPTION),
              new BigDecimal(request.getParameter(Utils.PARAM_MAX_PRICE)),
              new BigDecimal(request.getParameter(Utils.PARAM_MIN_PRICE)),
              new BigDecimal(request.getParameter(Utils.PARAM_FINAL_PRICE)), null, new Integer(
                  request.getParameter(Utils.PARAM_STOCK)), pc, null, null);
        } else {
          renderError(response);
          return;
        }
      } catch (Exception e) {
        e.printStackTrace();
        renderError(response);
        return;
      }
    } else if (action.equals("delete")) {
      try {
        dbhelper.removeProductById(idS);
      } catch (Exception e) {
        e.printStackTrace();
        renderError(response);
        return;
      }
    } else {
      renderError(response);
      return;
    }
  }


  private void renderError(HttpServletResponse response) {
    // TODO Return error code for AJAX
  }
}
