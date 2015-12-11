package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import es.uc3m.tiw.g01.model.entities.Product;

import javax.persistence.ManyToOne;

import es.uc3m.tiw.g01.model.entities.Client;

@Entity
@Table(name = "Subscription")
public class Subscription implements Serializable {

  private static final long serialVersionUID = 1L;

  public Subscription() {}

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  // bi-directional many-to-one association to Product
  @ManyToOne
  private Product product;

  // bi-directional many-to-one association to Client
  @ManyToOne
  private Client client;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product param) {
    this.product = param;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client param) {
    this.client = param;
  }

}
