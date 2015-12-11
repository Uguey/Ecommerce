package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the purchase_items database table.
 * 
 */
@Entity
@Table(name = "purchase_items")
@NamedQuery(name = "PurchaseItem.findAll", query = "SELECT p FROM PurchaseItem p")
public class PurchaseItem implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int quantity;

  // bi-directional many-to-one association to Purchase
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchase_id")
  private Purchase purchas;

  // bi-directional many-to-one association to Product
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = true)
  private Product product;

  // price per unit of this item that was paid by the client (i.e., minus discounts)
  @Column(precision = 10, scale = 2)
  private BigDecimal productPrice;

  private String supplierCompany;

  private String productName;

  @Column(precision = 10, scale = 2)
  private BigDecimal margin;

  private boolean marginNeedsDiscount;

  private int supplierRef;

  // If this item has been reconciled with the bank
  private boolean reconciled;

  public PurchaseItem() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Purchase getPurchas() {
    return this.purchas;
  }

  public void setPurchas(Purchase purchas) {
    this.purchas = purchas;
  }

  public Product getProduct() {
    return this.product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(BigDecimal productPrice) {
    this.productPrice = productPrice;
  }

  public String getSupplierCompany() {
    return supplierCompany;
  }

  public void setSupplierCompany(String param) {
    this.supplierCompany = param;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String param) {
    this.productName = param;
  }

  public BigDecimal getMargin() {
    return margin;
  }

  public void setMargin(BigDecimal param) {
    this.margin = param;
  }

  public boolean isMarginNeedsDiscount() {
    return marginNeedsDiscount;
  }

  /**
   * @return Total revenue the admin gets for this product item (taking quantity into account).
   */
  public BigDecimal getAdminRevenue() {
    return margin.multiply(new BigDecimal(quantity));
  }

  /**
   * @return Total revenue the supplier gets for this product item (taking quantity into account).
   */
  public BigDecimal getSupplierRevenue() {
    return productPrice.subtract(margin).multiply(new BigDecimal(quantity));
  }

  public void setMarginNeedsDiscount(boolean marginNeedsDiscount) {
    this.marginNeedsDiscount = marginNeedsDiscount;
  }

  public int getSupplierRef() {
    return supplierRef;
  }

  public void setSupplierRef(int param) {
    this.supplierRef = param;
  }

  public boolean isReconciled() {
    return reconciled;
  }

  public void setReconciled(boolean reconciled) {
    this.reconciled = reconciled;
  }

}
