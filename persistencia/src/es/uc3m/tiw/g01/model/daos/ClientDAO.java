package es.uc3m.tiw.g01.model.daos;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Product;

public interface ClientDAO {

  /**************************************************/
  /******************** CLIENT **********************/
  /**************************************************/

  public abstract boolean createClient(Client newClient) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException;

  public abstract Client createClient(String name, String surname, String email, String pass,
      String pwdrecId) throws NotSupportedException, SystemException, SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException;

  public abstract void removeClientById(int id) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  public abstract void updateClientById(int id, String name, String surname, String email,
      String address) throws SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException;

  public abstract void updateClientById(int id, Client modifiedFields) throws SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException;

  public abstract List<Client> findAllClients();

  public abstract Client findClientById(int id);

  public abstract Client findClientByEmail(String email);

  /**************************************************/
  /**************** SHOPPING CART *******************/
  /**************************************************/

  /** @return true if the shopping cart of a client contains the given product **/
  public abstract boolean shoppingCartContainsProduct(Client c, Product p)
      throws SecurityException, IllegalStateException, NotSupportedException, SystemException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  /**
   * Adds the given product in the given quantity to a given client's shopping cart.
   * 
   * @return The remaining number of elements in the shopping cart of that client.
   */
  public abstract int addToShoppingCart(Client c, Product p, int quantity)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  /**
   * Removes the given product in the given quantity from a given client's shopping cart.
   * 
   * @param quantity Quantity of items to remove from the cart, or -1 to remove all.
   * 
   * @return The remaining number of elements in the shopping cart of that client.
   */
  public abstract int removeFromShoppingCart(Client c, Product p, int quantity)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException;

  /**
   * @return the products in the client's shopping cart.
   */
  public abstract ArrayList<Product> getShoppingCart(Client c) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException;

  /**
   * @return the number of products in the client's shopping cart.
   */
  public abstract int numItemsInShoppingCart(Client c) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException;
}
