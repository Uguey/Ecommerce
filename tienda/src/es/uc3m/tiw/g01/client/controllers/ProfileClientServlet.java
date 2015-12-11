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

/**
 * Controller for displaying and editing the user's info. TODO: Finish (see issue #69)
 */
@WebServlet("/profile")
public class ProfileClientServlet extends BaseDbServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Renders the profile page for the logged in user.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client authenticatedClient = dbhelper.findAuthenticatedClient(request);

    if (authenticatedClient != null) {
      request.setAttribute("client", authenticatedClient);
      if (authenticatedClient.getAddress() != null) {
        String[] parts = authenticatedClient.getAddress().split("%");
        // showing blank address if spaces
        if (parts[0].equals(" "))
          parts[0] = "";
        if (parts[1].equals(" "))
          parts[1] = "";
        if (parts[2].equals(" "))
          parts[2] = "";
        request.setAttribute("address", parts[0]);
        request.setAttribute("postalCode", parts[1]);
        request.setAttribute("city", parts[2]);
      }

      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.CLIENT_PROFILE)
          .forward(request, response);
    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
    }
  }

  /**
   * Saves the user's email and password
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    Client old = dbhelper.findAuthenticatedClient(request);
    if (old != null) {
      Client modifiedFields = Client.parseFromRequest(request);
      if (modifiedFields != null) {
        try {
          dbhelper.updateClientById(old.getId(), modifiedFields);
        } catch (Exception e) {
          e.printStackTrace();
          request.setAttribute("validation_error", "Error updating client");
          doGet(request, response);
          return;
        }

        // Set user as logged in
        HttpSession session = request.getSession();
        session.setAttribute("email", modifiedFields.getEmail());

        request.setAttribute("client", modifiedFields);
      }
    } else {
      getServletConfig().getServletContext().getRequestDispatcher(Routes.JSP.ERROR)
          .forward(request, response);
      return;
    }

    doGet(request, response);
  }

}
