package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Supplier;
import es.uc3m.tiw.g01.supplier.routes.Routes;

/**
 * Servlet implementation class ProfileSupplierServlet
 */
@WebServlet("/profile")
public class ProfileSupplierServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Show users profile
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Supplier authenticatedSupplier = dbhelper.findAuthenticatedSupplier(request);
    if (authenticatedSupplier != null) {
      request.setAttribute("supplier", authenticatedSupplier);
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.SUPPLIER_PROFILE)
          .forward(request, response);
    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
    }
  }

  /**
   * Save new user info
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Supplier old = dbhelper.findAuthenticatedSupplier(request);
    if (old != null) {
      Supplier newSupplier = Supplier.parseFromRequest(request);
      if (newSupplier != null) {
        // Delete old supplier
        try {
          dbhelper.updateSupplierById(old.getId(), newSupplier.getEmail(),
              newSupplier.getCompanyName(), newSupplier.getHashPwd());
        } catch (Exception e) {
          e.printStackTrace();
        }
        // Set user as logged in
        HttpSession session = request.getSession();
        session.setAttribute("email", newSupplier.getEmail());
        request.setAttribute("supplier", newSupplier);
      }
    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.LOGIN)
          .forward(request, response);
    }

    // Redirect to profile page
    doGet(request, response);
  }

}
