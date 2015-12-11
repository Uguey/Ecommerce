package es.uc3m.tiw.g01.orders;

import javax.ejb.Local;

@Local
public interface ConciliationManagerLocal {

	public int reconcileCompany(int month, int year);
	
	public int reconcileSuppliers(int month, int year);
	
}
