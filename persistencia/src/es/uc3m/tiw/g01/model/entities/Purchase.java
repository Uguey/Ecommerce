package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.CascadeOnDelete;

/**
 * The persistent class for the purchases database table.
 * 
 */
@Entity
@Table(name = "purchases")
@CascadeOnDelete
@NamedQuery(name = "Purchase.findAll", query = "SELECT p FROM Purchase p")
public class Purchase implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private BigDecimal totalPrice;

  @Column(name = "delivery_fulladdress")
  private String deliveryFulladdress;

  @Column(name = "purchase_date")
  private Timestamp purchaseDate;

  // bi-directional many-to-one association to PurchaseItem
  @OneToMany(mappedBy = "purchas", cascade = CascadeType.ALL)
  private List<PurchaseItem> purchaseItems;

  // bi-directional many-to-one association to Client
  @ManyToOne(fetch = FetchType.LAZY)
  private Client client;

  private BigDecimal discount;

  // If the client has paid or not
  private boolean bought;

  // If the admin has made the split
  private boolean splitted;

  public Purchase() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isBought() {
    return bought;
  }

  public void setBought(boolean bought) {
    this.bought = bought;
  }

  public boolean isSplitted() {
    return splitted;
  }

  public void setSplitted(boolean splitted) {
    this.splitted = splitted;
  }

  public String getDeliveryFulladdress() {
    return this.deliveryFulladdress;
  }

  public void setDeliveryFulladdress(String deliveryFulladdress) {
    this.deliveryFulladdress = deliveryFulladdress;
  }

  public Timestamp getPurchaseDate() {
    return this.purchaseDate;
  }

  public void setPurchaseDate(Timestamp purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  public List<PurchaseItem> getPurchaseItems() {
    return this.purchaseItems;
  }

  public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
    this.purchaseItems = purchaseItems;
  }

  public PurchaseItem addPurchaseItem(PurchaseItem purchaseItem) {
    getPurchaseItems().add(purchaseItem);
    purchaseItem.setPurchas(this);

    return purchaseItem;
  }

  public PurchaseItem removePurchaseItem(PurchaseItem purchaseItem) {
    getPurchaseItems().remove(purchaseItem);
    purchaseItem.setPurchas(null);

    return purchaseItem;
  }

  public Client getClient() {
    return this.client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal param) {
    this.discount = param;
  }

}
