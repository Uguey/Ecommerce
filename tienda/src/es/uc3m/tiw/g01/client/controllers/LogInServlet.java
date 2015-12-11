package es.uc3m.tiw.g01.client.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.uc3m.tiw.g01.client.routes.Routes;
import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.ProductCategory;
import es.uc3m.tiw.g01.model.utils.Utils;

/**
 * Login servlet
 */
@WebServlet("/login")
public class LogInServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Tries to authenticate the user and if successful, adds it to the session.
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    String passwordHash = Utils.hashPassword(password);

    Client candidateClient = dbhelper.findClientByEmail(email);

    HttpSession session = request.getSession();

    if (candidateClient != null && Utils.validateNotEmpty(email)
        && email.equalsIgnoreCase(candidateClient.getEmail()) && Utils.validateNotEmpty(password)
        && Utils.validateSamePassword(passwordHash, candidateClient.getHashPwd())) {
      // Mark the user as authenticated by persisting his email in the
      // session.
      session.setAttribute("email", email);

      // Set the initial value of the badge in the shopping cart icon.
      String numItemsInCart = "";
      try {
        numItemsInCart = String.valueOf(dbhelper.numItemsInShoppingCart(candidateClient));
      } catch (Exception e) {
        e.printStackTrace();
      }
      response.addCookie(new Cookie("num_items_in_cart", numItemsInCart));

      // Redirect user back to the home page.
      response.sendRedirect("");
    } else {
      // Show user the home page, with an error

      session.removeAttribute("email");
      session.invalidate();
      if ((!Utils.validateNotEmpty(email)) && (!Utils.validateNotEmpty(password))) {
        request.setAttribute("badCredentials",
            "The user and the password are empty, please fill in these fields");
      }

      else if (!Utils.validateNotEmpty(email) && (Utils.validateNotEmpty(password))) {
        request.setAttribute("badCredentials", "The user is empty, please fill in this field");
      } else if (Utils.validateNotEmpty(email) && !Utils.validateNotEmpty(password)) {
        request.setAttribute("badCredentials", "The password is empty, please fill in this field");
      } else {
        request.setAttribute("badCredentials",
            "The user or the password is wrong, please use a correct email or password");
      }

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
}
