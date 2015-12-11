package es.uc3m.tiw.g01.orders;

import javax.ejb.Local;

@Local
public interface OrderManagerLocal {
	public String generateOrderCode();
}
