package es.uc3m.tiw.g01.orders;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import es.uc3m.tiw.g01.bank.requests.ConciliationRequest;
import es.uc3m.tiw.g01.bank.responses.ConciliationResponse;
import es.uc3m.tiw.g01.model.daos.ConciliationDAO;
import es.uc3m.tiw.g01.model.entities.PurchaseItem;
import es.uc3m.tiw.g01.model.entities.Supplier;

/**
 * Session Bean implementation class ConciliationManager
 */
@Stateless
@LocalBean
public class ConciliationManager implements ConciliationManagerLocal,
		ConciliationDAO {

	@PersistenceContext(unitName = "persistencia")
	private EntityManager em;

	@Override
	public int reconcileCompany(int month, int year) {
		List<PurchaseItem> unreconciledPurchasedItems = getAllUnreconciledPurchases(
				month - 1, year);

		if (unreconciledPurchasedItems.size() > 0) {

			BigDecimal amountToReconcile = getTotalAdminRevenueFrom(unreconciledPurchasedItems);

			String url = "http://localhost:8080/BancoWEB/conciliation/company";
			Client client = ClientBuilder.newClient();
			WebTarget web = client.target(url);
			Invocation.Builder builder = web.request(MediaType.APPLICATION_JSON);
			ConciliationRequest request = new ConciliationRequest();
			request.setMonth(month);
			request.setYear(year);
			request.setAmount(amountToReconcile);			
			Entity<ConciliationRequest> requestJSON = Entity.entity(request, MediaType.APPLICATION_JSON);
			ConciliationResponse res = builder.post(requestJSON, ConciliationResponse.class);
			return res.getResult();
		} else {
			return ConciliationResponse.RESULT_CODE_OK;
		}
	}
	
	@Override
	public int reconcileSuppliers(int month, int year) {
		List<Supplier> suppliers = em.createQuery("SELECT s FROM Supplier s")
				.getResultList();

		for (Supplier supplier : suppliers) {
			List<PurchaseItem> unreconciledPurchasedItems = getAllUnreconciledPurchases(
					month - 1, year, supplier.getId());

			if (unreconciledPurchasedItems.size() > 0) {

				BigDecimal amountToReconcile = getTotalSupplierRevenueFrom(unreconciledPurchasedItems);
				
				String url = "http://localhost:8080/BancoWEB/conciliation/bank";
				Client client = ClientBuilder.newClient();
				WebTarget web = client.target(url);
				Invocation.Builder builder = web.request(MediaType.APPLICATION_JSON);
				ConciliationRequest request = new ConciliationRequest();
				request.setMonth(month);
				request.setYear(year);
				request.setAmount(amountToReconcile);
				request.setSupplierCode(String.valueOf(supplier.getId()));
				Entity<ConciliationRequest> requestJSON = Entity.entity(request, MediaType.APPLICATION_JSON);
				ConciliationResponse res = builder.post(requestJSON, ConciliationResponse.class);
				
				if (res.getResult() == ConciliationResponse.RESULT_CODE_OK) {
					setAsReconciled(unreconciledPurchasedItems);
				} else {
					return res.getResult();
				}
			}
		}

		return ConciliationResponse.RESULT_CODE_OK;
	}
	
	/*************************************************************************
	 *************************** CONCILIATION DAO ****************************
	 *************************************************************************/

	@Override
	public List<PurchaseItem> getAllUnreconciledPurchases(int month, int year) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0);
		Date startDate = c.getTime();
		c.add(Calendar.MONTH, 1);
		Date endDate = c.getTime();

		Query q = em.createQuery("SELECT pi FROM PurchaseItem pi "
				+ "WHERE pi.reconciled = :reconciled "
				+ "AND pi.purchas.purchaseDate >= :startDate "
				+ "AND pi.purchas.purchaseDate < :endDate");
		q.setParameter("reconciled", false);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);

		return q.getResultList();
	}

	@Override
	public List<PurchaseItem> getAllUnreconciledPurchases(int month, int year,
			int supplierId) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1, 0, 0);
		Date startDate = c.getTime();
		c.add(Calendar.MONTH, 1);
		Date endDate = c.getTime();

		Query q = em.createQuery("SELECT pi FROM PurchaseItem pi "
				+ "WHERE pi.reconciled = :reconciled "
				+ "AND pi.purchas.purchaseDate >= :startDate "
				+ "AND pi.purchas.purchaseDate < :endDate "
				+ "AND pi.supplierRef = :supplierId");
		q.setParameter("reconciled", false);
		q.setParameter("supplierId", supplierId);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);

		return q.getResultList();
	}
	
	@Override
	public void setAsReconciled(List<PurchaseItem> unreconciledPurchasedItems) {
		for (PurchaseItem purchaseItem : unreconciledPurchasedItems) {
			purchaseItem.setReconciled(true);
			em.merge(purchaseItem);
		}	
	}

	private BigDecimal getTotalAdminRevenueFrom(
			List<PurchaseItem> purchasedItems) {
		BigDecimal total = new BigDecimal(0);

		for (PurchaseItem purchaseItem : purchasedItems) {
			total = total.add(purchaseItem.getAdminRevenue());
		}

		return total;
	}

	private BigDecimal getTotalSupplierRevenueFrom(
			List<PurchaseItem> purchasedItems) {
		BigDecimal total = new BigDecimal(0);

		for (PurchaseItem purchaseItem : purchasedItems) {
			total = total.add(purchaseItem.getSupplierRevenue());
		}

		return total;
	}

}
