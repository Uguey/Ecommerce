package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import es.uc3m.tiw.g01.model.utils.Utils;

/**
 * The persistent class for the clients database table.
 * 
 */
@Entity
@Table(name = "clients")
@CascadeOnDelete
@NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
public class Client implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String email;

  @Column(name = "hash_pwd")
  private String hashPwd;

  private String name;

  @Column(name = "pwd_recovery_id")
  private BigInteger pwdRecoveryId;

  @Column(name = "signup_date")
  private Timestamp signupDate;

  private String surname;

  private String address;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  // bi-directional many-to-one association to Purchase
  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private List<Purchase> purchases;

  // bi-directional many-to-one association to Subscription
  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private List<Subscription> subscription;

  // bi-directional many-to-one association to Coupon
  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private List<Coupon> coupons;

  public Client() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHashPwd() {
    return this.hashPwd;
  }

  public void setHashPwd(String hashPwd) {
    this.hashPwd = hashPwd;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigInteger getPwdRecoveryId() {
    return this.pwdRecoveryId;
  }

  public void setPwdRecoveryId(BigInteger pwdRecoveryId) {
    this.pwdRecoveryId = pwdRecoveryId;
  }

  public Timestamp getSignupDate() {
    return this.signupDate;
  }

  public void setSignupDate(Timestamp signupDate) {
    this.signupDate = signupDate;
  }

  public String getSurname() {
    return this.surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public List<Purchase> getPurchases() {
    return this.purchases;
  }

  public void setPurchases(List<Purchase> purchases) {
    this.purchases = purchases;
  }

  public Purchase addPurchas(Purchase purchas) {
    getPurchases().add(purchas);
    purchas.setClient(this);

    return purchas;
  }

  public Purchase removePurchas(Purchase purchas) {
    getPurchases().remove(purchas);
    purchas.setClient(null);

    return purchas;
  }

  public static Client parseFromRequest(HttpServletRequest request) {

    String email = request.getParameter(Utils.PARAM_EMAIL);
    if (!Utils.validateNotEmpty(email) || !Utils.validateEmail(email)) {
      request.setAttribute("validation_error", "Email address not valid");
      return null;
    }

    String name = request.getParameter(Utils.PARAM_NAME);
    if (!Utils.validateNotEmpty(name)) {
      request.setAttribute("validation_error", "Name can't be empty");
      return null;
    }
    String surname = request.getParameter(Utils.PARAM_SURNAME);
    if (!Utils.validateNotEmpty(surname)) {
      request.setAttribute("validation_error", "Surname can't be empty");
      return null;
    }

    String password = request.getParameter(Utils.PARAM_PASSWORD);
    if (!Utils.validateNotEmpty(password)) {
      request.setAttribute("validation_error", "Password can't be empty");
      return null;
    }

    String password2 = request.getParameter(Utils.PARAM_PASSWORD_CONFIRMATION);
    if (!Utils.validateNotEmpty(password2)) {
      request.setAttribute("validation_error", "Password can't be empty");
      return null;
    }
    if (!Utils.validateSamePassword(password, password2)) {
      request.setAttribute("validation_error", "Passwords must be the same");
      return null;
    }
    String address = request.getParameter("address");
    String postalCode = request.getParameter("postalCode");
    String city = request.getParameter("city");

    // setting " " if address is empty to avoid nullPointer split
    if (!Utils.validateNotEmpty(address)) {
      address = " ";
    }
    if (!Utils.validateNotEmpty(postalCode)) {
      postalCode = " ";
    } else if (!Utils.validateNumber(postalCode, true, true) || postalCode.length() != 5) {
      request
          .setAttribute("validation_error", "Postal code can't be empty and has to be 5 numbers");
      return null;
    }
    if (!Utils.validateNotEmpty(city)) {
      city = " ";
    }

    String fullAddress = address + "%" + postalCode + "%" + city;

    Client c = new Client();
    c.setEmail(email);
    c.setName(name);
    c.setSurname(surname);
    c.setAddress(fullAddress);
    c.setHashPwd(Utils.hashPassword(password));
    c.setSignupDate(new Timestamp(new Date().getTime()));
    return c;

  }

  public List<Subscription> getSubscription() {
    return subscription;
  }

  public void setSubscription(List<Subscription> param) {
    this.subscription = param;

  }

}
