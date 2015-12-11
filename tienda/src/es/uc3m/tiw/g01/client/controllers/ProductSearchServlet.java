package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.utils.Utils;

/**
 * Controller in charge of showing the search page and search results.
 */
@WebServlet("/search")
public class ProductSearchServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  private static final String[] VALID_CATEGORY_OPS = new String[] {"AND", "OR"};
  private static final String[] VALID_SUPPLIER_OPS = new String[] {"AND", "OR"};
  private static final String[] VALID_PRICE_OPS = new String[] {"<", ">", "=", "ANY"};

  /**
   * Renders the search page (in which the user inputs the search params).
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Populate list of categories
    request.setAttribute("categories", dbhelper.findAllProductCategories());

    // Populate list of suppliers
    request.setAttribute("suppliers", dbhelper.findAllSuppliers());

    getServletContext().getRequestDispatcher(Routes.JSP.SEARCH).forward(request, response);
  }

  /**
   * Performs a search and renders the results.
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Parse and validate query.
    // 1. Product name. We allow an empty one.
    String name = request.getParameter(Utils.PARAM_NAME);
    if (name == null) {
      name = "";
    }

    // 2. Product category. If ID comes empty, we set it to -1 (which allows any ID).
    int categoryId;
    if (Utils.validateNumber(request.getParameter(Utils.PARAM_CATEGORY), true, true)) {
      categoryId = Integer.parseInt(request.getParameter(Utils.PARAM_CATEGORY));
    } else {
      categoryId = -1;
    }

    // 2b. Product category operation. If invalid, we set the ID to -1
    String categoryOp = request.getParameter(Utils.PARAM_CATEGORY_OP);
    if (!Utils.validateEnum(VALID_CATEGORY_OPS, categoryOp)) {
      categoryId = -1;
    }

    // 3. Supplier. If ID comes empty, we set it to -1 (which allows any ID).
    int supplierId;
    if (Utils.validateNumber(request.getParameter(Utils.PARAM_SUPPLIER), true, true)) {
      supplierId = Integer.parseInt(request.getParameter(Utils.PARAM_SUPPLIER));
    } else {
      supplierId = -1;
    }

    // 3b. Supplier operation. If invalid, we set the ID to -1
    String supplierOp = request.getParameter(Utils.PARAM_SUPPLIER_OP);
    if (!Utils.validateEnum(VALID_SUPPLIER_OPS, supplierOp)) {
      supplierId = -1;
    }

    // 4. Price operation. If invalid, we set it to ANY
    String priceOp = request.getParameter(Utils.PARAM_PRICE_OP);
    if (!Utils.validateEnum(VALID_PRICE_OPS, priceOp)) {
      priceOp = "ANY";
    }

    // 4b. Price. If invalid, we set the operation to ANY
    BigDecimal price;
    if (Utils.validateNumber(request.getParameter(Utils.PARAM_PRICE), false, false)) {
      price = new BigDecimal(request.getParameter(Utils.PARAM_PRICE));
    } else {
      price = new BigDecimal(0);
      priceOp = "ANY";
    }

    // Query the DB.
    List<Product> queryResults =
        dbhelper.findProductsByQuery(name, categoryOp, categoryId, supplierOp, supplierId, priceOp,
            price);

    // Feed results to JSP.
    if (queryResults.size() > 0) {
      request.setAttribute("products", queryResults);
      request.setAttribute("category_name", "Search results:");

      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.CATALOG)
          .forward(request, response);
    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.NO_RESULTS)
          .forward(request, response);
    }
  }
}
