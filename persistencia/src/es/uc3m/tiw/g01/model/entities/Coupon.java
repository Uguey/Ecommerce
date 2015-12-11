package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: Coupon
 *
 */
@Entity
public class Coupon implements Serializable {


  private static final long serialVersionUID = 1L;

  public static final int COUPON_TYPE_10 = 10;
  public static final int COUPON_TYPE_20 = 20;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private Timestamp date;

  // bi-directional many-to-one association to Client
  @ManyToOne(fetch = FetchType.LAZY)
  private Client client;

  private int type;

  private boolean isUsed;

  private String code;



  public Coupon() {}

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder().append("$");

    switch (type) {
      case COUPON_TYPE_10:
        sb.append("10");
        break;
      case COUPON_TYPE_20:
        sb.append("20");
        break;
    }

    sb.append(" coupon");

    return sb.toString();
  }

  public boolean isUsed() {
    return isUsed;
  }

  public void setUsed(boolean isUsed) {
    this.isUsed = isUsed;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return The discount of the coupon, up to 10% of a given total price.
   */
  public BigDecimal getRedeemableValue(BigDecimal totalPrice) {
    BigDecimal discount = new BigDecimal(getType());
    discount = discount.min(totalPrice.multiply(new BigDecimal("0.1")));
    return discount.setScale(2, RoundingMode.CEILING);
  }
}
