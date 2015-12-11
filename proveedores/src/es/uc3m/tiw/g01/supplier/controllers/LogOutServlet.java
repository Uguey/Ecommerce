package es.uc3m.tiw.g01.supplier.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogOut
 */
@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Clears the session
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession();
    session.removeAttribute("email");
    session.invalidate();

    response.sendRedirect("");
  }

}
