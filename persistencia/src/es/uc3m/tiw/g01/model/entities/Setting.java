package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "Settings")
public class Setting implements Serializable {

  private static final long serialVersionUID = 1L;

  public Setting() {}

  @Id
  private long id;
  private String email;
  private String password;
  private int stockThresHoldLow;
  private int stockThresHoldVeryLow;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String param) {
    this.email = param;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String param) {
    this.password = param;
  }

  public int getStockThresHoldLow() {
    return stockThresHoldLow;
  }

  public void setStockThresHoldLow(int param) {
    this.stockThresHoldLow = param;
  }

  public int getStockThresHoldVeryLow() {
    return stockThresHoldVeryLow;
  }

  public void setStockThresHoldVeryLow(int param) {
    this.stockThresHoldVeryLow = param;
  }

}
