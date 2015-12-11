package es.uc3m.tiw.g01.model.daos;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.ProductCategory;
import es.uc3m.tiw.g01.model.entities.Supplier;

public interface ProductDAO {

  /**************************************************/
  /******************* PRODUCT **********************/
  /**************************************************/

  public abstract Product createProduct(String name, String description, String finalPrice,
      String maxPrice, String minPrice, String stock, ProductCategory pc, Supplier s,
      Boolean onSale, String image) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  /**
   * @return true if product found and deleted.
   */
  public abstract void removeProductById(String idS) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  public abstract void removeProductById(int id) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  public abstract Product updateProductById(int id, String name, String description,
      BigDecimal maxPrice, BigDecimal minPrice, BigDecimal finalPrice, Boolean onSale,
      Integer stock, ProductCategory pc, BigDecimal margin, Boolean marginIsPercentage)
      throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException;

  public abstract List<Product> findAllProducts();

  public abstract Product findProductById(String id) throws NotSupportedException, SystemException;

  public abstract Product findProductById(int id) throws NotSupportedException, SystemException;

  /**
   * @return List of all the products of a category, independently on whether they have stock.
   */
  public abstract List<Product> findProductsByCategory(ProductCategory pc);

  /**
   * @return List of all the products of a category which have positive stock.
   */
  public abstract List<Product> findAvailableProductsByCategory(ProductCategory pc);

  /**
   * @return List of the products currently on sale and with stock.
   */
  public abstract List<Product> findProductsOnSale();

  /**
   * Save a new product in the db.
   * 
   * @return Persisted product.
   */
  public abstract Product saveNewProduct(Product p) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  /**
   * Perform a query with the given params.
   * 
   * @return list with results.
   */
  public abstract List<Product> findProductsByQuery(String name, String categoryOp, int categoryId,
      String supplierOp, int supplierId, String priceOp, BigDecimal price);

  public abstract void updateProductMarginBySupplier(BigDecimal margin, Boolean marginIsPercentage,
      int supplierId) throws SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException;

  public void updateProductMargin(BigDecimal margin, Boolean marginIsPercentage)
      throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException;

}
