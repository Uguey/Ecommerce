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
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterSupplierServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.REGISTRATION)
        .forward(request, response);
  }

  /**
   * Persist a supplier in the DB
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Supplier s = Supplier.parseFromRequest(request);

    if (s != null) {
      boolean newSupplier = false;
      // Persist in DB
      try {
        newSupplier = dbhelper.createSupplier(s);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (newSupplier) {
        // Set user as logged in
        HttpSession session = request.getSession();
        session.setAttribute("email", s.getEmail());
        // Redirect home with a success
        request.setAttribute("result", "ok");
      } else
        request.setAttribute("result", "oldSupplier");
    } else {
      request.setAttribute("result", "error");
    }

    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.REGISTRATION)
        .forward(request, response);
  }
}
