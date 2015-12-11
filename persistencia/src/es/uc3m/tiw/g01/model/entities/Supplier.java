package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import es.uc3m.tiw.g01.model.utils.Utils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the suppliers database table.
 * 
 */
@Entity
@Table(name = "suppliers")
@Cacheable
@CascadeOnDelete
@NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s")
public class Supplier implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "company_name")
  private String companyName;

  private String email;

  @Column(name = "hash_pwd")
  private String hashPwd;

  @Column(name = "pwd_recovery_id")
  private BigInteger pwdRecoveryId;

  @Column(name = "signup_date")
  private Timestamp signupDate;

  // bi-directional many-to-one association to Product
  @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
  private List<Product> products;

  public Supplier() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCompanyName() {
    return this.companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
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

  public List<Product> getProducts() {
    return this.products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public Product addProduct(Product product) {
    getProducts().add(product);
    product.setSupplier(this);

    return product;
  }

  public Product removeProduct(Product product) {
    getProducts().remove(product);
    product.setSupplier(null);

    return product;
  }

  public static Supplier parseFromRequest(HttpServletRequest request) {
    String email = request.getParameter(Utils.PARAM_EMAIL);
    String password = request.getParameter(Utils.PARAM_PASSWORD);
    String passwordConfirmation = request.getParameter(Utils.PARAM_PASSWORD_CONFIRMATION);
    String companyName = request.getParameter(Utils.PARAM_COMPANY_NAME);

    if (!Utils.validateNotEmpty(email)) {
      request.setAttribute("badCredentials", "Please fill in the Email field");
      return null;
    }

    if (!Utils.validateEmail(email)) {
      request.setAttribute("badCredentials", "The Email field has incorrect format");
      return null;
    }

    if (!Utils.validateNotEmpty(password)) {
      request.setAttribute("badCredentials", "Please fill in the Password field");
      return null;
    }

    if (!Utils.validateNotEmpty(passwordConfirmation)) {
      request.setAttribute("badCredentials", "Please fill in the Password Confirmation field");
      return null;
    }
    if (!Utils.validateNotEmpty(companyName)) {
      request.setAttribute("badCredentials", "Please fill in the Company Name field");
      return null;
    }
    if (!Utils.validateSamePassword(password, passwordConfirmation)) {
      request.setAttribute("badCredentials", "The passwords do not match");
      return null;
    }

    Supplier s = new Supplier();
    s.setEmail(email);
    s.setHashPwd(Utils.hashPassword(password));
    s.setCompanyName(companyName);
    s.setSignupDate(new Timestamp(new Date().getTime()));
    return s;
  }

}
