package es.uc3m.tiw.g01.model.daos;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.entities.Supplier;

public interface SupplierDAO {

  /**************************************************/
  /******************* SUPPLIER *********************/
  /**************************************************/

  public abstract boolean createSupplier(Supplier s) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  public abstract Supplier createSupplier(String email, String pass, String companyName,
      String pwdrecId) throws NotSupportedException, SystemException, SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException;

  public abstract void removeSupplierById(int id) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException;

  public abstract void updateSupplierById(int id, String email, String companyName,
      String hashedPass) throws SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException;

  public abstract List<Supplier> findAllSuppliers();

}
