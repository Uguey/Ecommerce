package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.uc3m.tiw.g01.controllers.BaseDbServlet;
import es.uc3m.tiw.g01.model.entities.Supplier;
import es.uc3m.tiw.g01.model.utils.Utils;
import es.uc3m.tiw.g01.supplier.routes.Routes;

@WebServlet("/login")
public class LogInServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Sends user to login screen
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.LOGIN)
        .forward(req, resp);
  }

  /**
   * Tries to authenticate the user.
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String email = request.getParameter(Utils.PARAM_EMAIL);
    String password = request.getParameter(Utils.PARAM_PASSWORD);

    String passwordHsh = Utils.hashPassword(password);

    Supplier candidateSupplier = dbhelper.findSupplierByEmail(email);

    HttpSession session = request.getSession();
    if (candidateSupplier != null && Utils.validateNotEmpty(email)
        && email.equalsIgnoreCase(candidateSupplier.getEmail()) && Utils.validateNotEmpty(password)
        && Utils.validateSamePassword(passwordHsh, candidateSupplier.getHashPwd())) {
      session.setAttribute("email", email);
      response.sendRedirect("");
    } else {
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

      this.getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.LOGIN)
          .forward(request, response);
    }
  }
}
