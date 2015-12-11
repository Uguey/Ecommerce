package es.uc3m.tiw.g01.model.daos;

import java.util.List;

import es.uc3m.tiw.g01.model.entities.PurchaseItem;

public interface ConciliationDAO {

  public List<PurchaseItem> getAllUnreconciledPurchases(int month, int year);

  public List<PurchaseItem> getAllUnreconciledPurchases(int month, int year, int supplierId);

  public void setAsReconciled(List<PurchaseItem> unreconciledPurchasedItems);

}
