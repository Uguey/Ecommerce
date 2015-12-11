package es.uc3m.tiw.g01.model.daos;

import es.uc3m.tiw.g01.model.entities.Purchase;

public interface OrderDAO {

  public abstract void splitter(Purchase p);

}
