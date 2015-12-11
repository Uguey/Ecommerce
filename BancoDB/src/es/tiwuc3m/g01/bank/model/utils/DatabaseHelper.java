package es.tiwuc3m.g01.bank.model.utils;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import es.tiwuc3m.g01.bank.model.daos.BankConciliationDAO;
import es.tiwuc3m.g01.bank.model.daos.CompanyConciliationDAO;
import es.tiwuc3m.g01.bank.model.daos.OrderDAO;
import es.tiwuc3m.g01.bank.model.entities.BankConciliation;
import es.tiwuc3m.g01.bank.model.entities.CompanyConciliation;
import es.tiwuc3m.g01.bank.model.entities.Order;

public class DatabaseHelper implements OrderDAO, BankConciliationDAO,
		CompanyConciliationDAO {

	private EntityManager em;

	public DatabaseHelper(EntityManager em) {
		this.em = em;
	}

	@Override
	public Order createOrder(String couponCode, String orderCode,
			String transactionCode, BigDecimal amount, String creditCard)
			throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException {

		Order o = new Order();
		o.setCouponCode(couponCode);
		o.setAmount(amount);
		o.setCreditCard(creditCard);
		o.setOrderCode(orderCode);
		o.setTransactionCode(transactionCode);

		em.persist(o);

		return o;
	}

	@Override
	public CompanyConciliation createCompanyConciliation(String date, BigDecimal amount)
			throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException {
		
		CompanyConciliation cc = new CompanyConciliation();
		cc.setAmount(amount);
		cc.setConciliationDate(date);
		
		em.persist(cc);
		
		return cc;
	}

	@Override
	public BankConciliation createBankConciliation(String date, BigDecimal amount,
			String supplierCode) throws NotSupportedException, SystemException,
			SecurityException, IllegalStateException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		
		BankConciliation bc = new BankConciliation();
		bc.setAmount(amount);
		bc.setConciliationDate(date);
		bc.setSupplierCode(supplierCode);
		
		em.persist(bc);
		
		return bc;
	}
}
