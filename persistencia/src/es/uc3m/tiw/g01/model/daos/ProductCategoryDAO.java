package es.uc3m.tiw.g01.model.daos;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.entities.ProductCategory;

public interface ProductCategoryDAO {

  /**************************************************/
  /******************* CATEGORY *********************/
  /**************************************************/

  public abstract ProductCategory createProductCategory(String name, String image)
      throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException;

  public abstract void removeProductCategoryById(int id) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException;

  public abstract void updateProductCategoryById(int id, String name) throws SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException;

  public abstract List<ProductCategory> findAllProductCategories();

  public abstract ProductCategory findProductCategoryById(String sId);

  public abstract ProductCategory findProductCategoryByName(String name);

  public abstract ProductCategory findProductCategoryByID(int id);

}
