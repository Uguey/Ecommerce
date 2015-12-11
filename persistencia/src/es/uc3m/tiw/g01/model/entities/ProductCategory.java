package es.uc3m.tiw.g01.model.entities;

import java.io.Serializable;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;

import java.util.List;


/**
 * The persistent class for the product_categories database table.
 * 
 */
@Entity
@Table(name = "product_categories")
@NamedQuery(name = "ProductCategory.findAll", query = "SELECT p FROM ProductCategory p")
public class ProductCategory implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Lob
  private String image;

  private String name;

  // bi-directional many-to-one association to ProductCategory
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_parent_id")
  private ProductCategory productCategory;

  // bi-directional many-to-one association to ProductCategory
  @OneToMany(mappedBy = "productCategory")
  private List<ProductCategory> productCategories;

  // bi-directional many-to-one association to Product
  @OneToMany(mappedBy = "productCategory")
  private List<Product> products;

  public ProductCategory() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ProductCategory getProductCategory() {
    return this.productCategory;
  }

  public void setProductCategory(ProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  public List<ProductCategory> getProductCategories() {
    return this.productCategories;
  }

  public void setProductCategories(List<ProductCategory> productCategories) {
    this.productCategories = productCategories;
  }

  public ProductCategory addProductCategory(ProductCategory productCategory) {
    getProductCategories().add(productCategory);
    productCategory.setProductCategory(this);

    return productCategory;
  }

  public ProductCategory removeProductCategory(ProductCategory productCategory) {
    getProductCategories().remove(productCategory);
    productCategory.setProductCategory(null);

    return productCategory;
  }

  public List<Product> getProducts() {
    return this.products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public Product addProduct(Product product) {
    getProducts().add(product);
    product.setProductCategory(this);

    return product;
  }

  public Product removeProduct(Product product) {
    getProducts().remove(product);
    product.setProductCategory(null);

    return product;
  }

}
