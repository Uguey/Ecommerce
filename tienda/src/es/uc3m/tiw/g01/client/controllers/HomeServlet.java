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
import es.uc3m.tiw.g01.model.entities.ProductCategory;

/**
 * Servlet implementation class homeServlet
 */
@WebServlet("")
public class HomeServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Sends the information about up to 5 offers and the category of the products to home.jsp
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Fetch products on sale
    int numProdsToShow = Math.min(5, dbhelper.findProductsOnSale().size());
    List<Product> productsOnSale = dbhelper.findProductsOnSale().subList(0, numProdsToShow);
    request.setAttribute("productsOnSale", productsOnSale);

    // Fetch all product categories
    List<ProductCategory> categories = dbhelper.findAllProductCategories();
    request.setAttribute("categories", categories);
    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.HOME)
        .forward(request, response);
  }
}
