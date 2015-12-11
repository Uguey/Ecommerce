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
import es.uc3m.tiw.g01.model.utils.Utils;

/**
 * Servlet for controlling Client registrations
 */
@WebServlet("/register")
public class RegisterClientServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.REGISTRATION)
        .forward(request, response);
  }

  /**
   * Persists a new client in the database
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client c = Client.parseFromRequest(request);

    if (c != null) {
      boolean newClient = false;
      // Persist in DB
      try {
        newClient = dbhelper.createClient(c);
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
      if (newClient) {
        // Set user as logged in
        HttpSession session = request.getSession();
        session.setAttribute("email", c.getEmail());
        // Redirect home with a success
        request.setAttribute("result", "ok");
      } else
        request.setAttribute("result", "oldClient");
    }
    getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.REGISTRATION)
        .forward(request, response);
  }
}
