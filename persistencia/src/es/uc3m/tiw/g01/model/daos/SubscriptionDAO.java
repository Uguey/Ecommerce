package es.uc3m.tiw.g01.model.daos;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.Subscription;
import es.uc3m.tiw.g01.model.entities.Supplier;

public interface SubscriptionDAO {
  public abstract Subscription createSubscription(Client client, Product prod)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  public abstract Subscription findSubscriptionByClientAndProduct(int clientId, int productId);

  public abstract void removeSubscriptionByClientAndProduct(Client client, Product prod)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  public abstract List<Subscription> findSubscriptionsByClient(Client client);

  public abstract List<Subscription> findSubscriptionsBySupplier(Supplier supplier);

}
