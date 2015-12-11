package es.uc3m.tiw.g01.orders;

import java.util.List;

import javax.ejb.Local;

import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Coupon;
import es.uc3m.tiw.g01.model.entities.Purchase;

@Local
public interface CouponManagerLocal {
	public List<Coupon> getRedeemableCoupons(Client authenticatedClient);

	public Coupon getCoupon(String couponId);

	public int generateCouponFor(Purchase p, Client c);
	
	public boolean redeemCoupon(Coupon cp);	
}
