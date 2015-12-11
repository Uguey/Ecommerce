package es.uc3m.tiw.g01.client.controllers;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.ProductCategory;

/**
 * Servlet for representing lists of products of a given category
 */
@WebServlet("/products")
public class ProductsServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Fetch a set of products only of the category requested, and send it to products.jsp
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session != null) {
      Client client = dbhelper.findAuthenticatedClient(request);
      if (client != null)
        request.setAttribute("email", client.getEmail());
    }

    ProductCategory category;

    try {
      category = dbhelper.findProductCategoryById(request.getParameter("cid"));
    } catch (NumberFormatException e) {
      // Id empty or NaN
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    // Category of the given id not found
    if (category == null) {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    request.setAttribute("products", dbhelper.findAvailableProductsByCategory(category));
    request.setAttribute("category_name", category.getName());

    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.CATALOG)
        .forward(request, response);
  }
}
