package es.uc3m.tiw.g01.orders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.daos.CouponDAO;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Coupon;
import es.uc3m.tiw.g01.model.entities.Purchase;

/**
 * Session Bean implementation class CouponManager
 */
@Stateless
public class CouponManager implements CouponManagerLocal, CouponDAO {

	@PersistenceContext(unitName = "persistencia")
  	private EntityManager em;	
    
    @Override
    public List<Coupon> getRedeemableCoupons(Client c) {
    	try {
    		Query q =
			  em.createQuery("SELECT cp FROM Coupon cp "
			    + "WHERE cp.client.id = :clientId and cp.isUsed = :isUsed and cp.date > :date");
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, -1);
			
			q.setParameter("clientId", c.getId());
			q.setParameter("isUsed", false);
			q.setParameter("date", cal.getTime());
			
			return q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return new ArrayList<Coupon>();
    }
    
    @Override
    public Coupon getCoupon(String couponId) {
    	if ("no_coupon".equals(couponId)) {
    		return null;
    	} else {
	    	return findCouponById(couponId);
    	}
    }
    
    @Override
    public int generateCouponFor(Purchase p, Client c) {
    	int couponValue = 0;

        try {
	        if (p.getTotalPrice().compareTo(new BigDecimal(100)) > 0) {
	          createCoupon(Coupon.COUPON_TYPE_20, c);
	          couponValue = Coupon.COUPON_TYPE_20;
	        } else if (p.getTotalPrice().compareTo(new BigDecimal(50)) > 0) {
	          createCoupon(Coupon.COUPON_TYPE_10, c);
	          couponValue = Coupon.COUPON_TYPE_10;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return couponValue;
    }
    
    /**************************************************/
    /****************** COUPON DAO ********************/
    /**************************************************/

    @Override
    public Coupon createCoupon(int type, Client c) throws NotSupportedException, SystemException,
        SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
        HeuristicRollbackException {

      Date now = new Date();
      Coupon coupon = new Coupon();
      coupon.setClient(c);
      coupon.setDate(new Timestamp(now.getTime()));
      coupon.setType(type);
      coupon.setUsed(false);
      coupon.setCode(generateCouponCode(now, type));

      em.persist(coupon);

      return coupon;
    }
    
    private String generateCouponCode(Date date, int type) {
        String s = "VALE";

        s += new SimpleDateFormat("yyyyMMddhhssSSSSa").format(date);

        switch (type) {
          case Coupon.COUPON_TYPE_10:
            s += "10";
            break;
          case Coupon.COUPON_TYPE_20:
            s += "20";
            break;
        }

        return s;
      }

    @Override
    public boolean redeemCoupon(Coupon cp) {
      if (cp == null || cp.isUsed()) {
        return false;
      }

      cp.setUsed(true);
      em.merge(cp);

      return true;
    }

    @Override
    public Coupon findCouponById(String sId) {
      if (sId == null || sId.length() == 0) {
        return null;
      }

      sId = sId.replaceAll("\t", "");
      sId = sId.replaceAll("\n", "");
      sId = sId.replaceAll(" ", "");
      Integer id = Integer.parseInt(sId);
      return em.find(Coupon.class, id);
    }
}
