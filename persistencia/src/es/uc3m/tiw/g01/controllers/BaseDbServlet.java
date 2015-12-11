package es.uc3m.tiw.g01.controllers;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.transaction.UserTransaction;

import es.uc3m.tiw.g01.model.utils.DatabaseHelper;

public abstract class BaseDbServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @PersistenceContext(unitName = "persistencia")
  private EntityManager em;
  @Resource
  private UserTransaction ut;

  protected DatabaseHelper dbhelper;

  @Override
  public void init() throws ServletException {
    dbhelper = new DatabaseHelper(em, ut);
  }
}
