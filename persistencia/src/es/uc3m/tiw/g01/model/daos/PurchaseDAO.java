package es.uc3m.tiw.g01.model.daos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Coupon;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.Purchase;

public interface PurchaseDAO {

  public abstract Purchase getShoppingCartPurchase(Client c) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  /**
   * Takes the clients current shopping cart and marks it as bought, applying an optional coupon to
   * pay it.
   * 
   * @return the bought purchase if everything went ok.
   */
  public abstract Purchase buyShoppingCart(Client c, String deliveryFullAddress, Coupon coupon)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  public abstract List<Purchase> getPaidPurchases(Client c, int orderCode)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  /* get shopping cart from purchase */
  public abstract ArrayList<Product> getProductsFromPurchase(Purchase p)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  public abstract Purchase findPurchaseByID(String sId);

  public abstract Purchase findPurchaseByID(int id);

  public abstract BigDecimal buyItemsFromPurchase(Purchase p) throws NotSupportedException,
      SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

}
