package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Product;

/**
 * Servlet for rendering the products on sale.
 */
@WebServlet("/offers")
public class OffersServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Render the catalog of products containing the products on sale.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    List<Product> productsOnSale = dbhelper.findProductsOnSale();
    request.setAttribute("products", productsOnSale);
    request.setAttribute("category_name", "Offers");

    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.CATALOG)
        .forward(request, response);
  }
}
