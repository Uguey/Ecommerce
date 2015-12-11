package es.tiwuc3m.g01.bank.model.daos;

import java.math.BigDecimal;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.tiwuc3m.g01.bank.model.entities.CompanyConciliation;

public interface CompanyConciliationDAO {
	public CompanyConciliation createCompanyConciliation(String date, BigDecimal amount)
			throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException;
}
