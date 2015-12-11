package es.uc3m.tiw.g01.json;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PaymentResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final int RESULT_CODE_OK = 1;
  public static final int RESULT_CODE_INVALID_CREDIT_CARD = 2;
  public static final int RESULT_CODE_MISSING_PARAMS = 3;
  public static final int RESULT_CODE_UNKNOWN = 4;
  private String couponCode;
  private String transactionCode;
  private String orderCode;
  private BigDecimal amount;
  private String creditCard;
  private int result;

  public PaymentResponse() {}

  public String getCouponCode() {
    return couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
  }

  public String getTransactionCode() {
    return transactionCode;
  }

  public void setTransactionCode(String transactionCode) {
    this.transactionCode = transactionCode;
  }

  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCreditCard() {
    return creditCard;
  }

  public void setCreditCard(String creditCard) {
    this.creditCard = creditCard;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }
}
