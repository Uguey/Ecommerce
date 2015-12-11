package es.uc3m.tiw.g01.model.daos;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Coupon;

public interface CouponDAO {

  public abstract Coupon createCoupon(int type, Client c) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException;

  public abstract boolean redeemCoupon(Coupon cp);

  public abstract List<Coupon> getRedeemableCoupons(Client c) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException;

  public abstract Coupon findCouponById(String sId);


}
