package es.tiwuc3m.g01.bank.model.daos;

import java.math.BigDecimal;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.tiwuc3m.g01.bank.model.entities.Order;

public interface OrderDAO {
	public Order createOrder(String couponCode, String orderCode,
			String transactionCode, BigDecimal amount, String creditCard)
			throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException;
}
