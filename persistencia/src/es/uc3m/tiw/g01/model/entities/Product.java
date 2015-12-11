package es.uc3m.tiw.g01.model.entities;

import static javax.persistence.FetchType.LAZY;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import es.uc3m.tiw.g01.model.utils.DatabaseHelper;
import es.uc3m.tiw.g01.model.utils.Utils;

/**
 * The persistent class for the products database table.
 * 
 */
@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  @Column(name = "final_price", precision = 10, scale = 2)
  private BigDecimal finalPrice;

  @Lob
  @Basic(fetch = LAZY)
  private String image;

  @Column(name = "max_price", precision = 10, scale = 2)
  private BigDecimal maxPrice;

  @Column(name = "min_price", precision = 10, scale = 2)
  private BigDecimal minPrice;

  private String name;

  @Column(name = "on_sale")
  private boolean onSale;

  private int stock;

  @Column(precision = 10, scale = 2)
  private BigDecimal margin;

  private boolean marginIsPercentage;

  // bi-directional many-to-one association to Supplier
  @ManyToOne(fetch = FetchType.LAZY)
  private Supplier supplier;

  // bi-directional many-to-one association to ProductCategory
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_category_id")
  private ProductCategory productCategory;

  // bi-directional many-to-one association to PurchaseItem
  @CascadeOnDelete
  @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
  private List<PurchaseItem> purchaseItems;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<Subscription> subscription;

  public Product() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getFinalPrice() {
    return this.finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public BigDecimal getMaxPrice() {
    return this.maxPrice;
  }

  public void setMaxPrice(BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
  }

  public BigDecimal getMinPrice() {
    return this.minPrice;
  }

  public void setMinPrice(BigDecimal minPrice) {
    this.minPrice = minPrice;
  }

  /**
   * @return If product is on sale, minPrice, else, finalPrice
   */
  public BigDecimal getRetailPrice() {
    return onSale ? minPrice : finalPrice;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean getOnSale() {
    return this.onSale;
  }

  public void setOnSale(boolean onSale) {
    this.onSale = onSale;
  }

  public int getStock() {
    return this.stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public Supplier getSupplier() {
    return this.supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public ProductCategory getProductCategory() {
    return this.productCategory;
  }

  public void setProductCategory(ProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  public List<PurchaseItem> getPurchaseItems() {
    return this.purchaseItems;
  }

  public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
    this.purchaseItems = purchaseItems;
  }

  public PurchaseItem addPurchaseItem(PurchaseItem purchaseItem) {
    getPurchaseItems().add(purchaseItem);
    purchaseItem.setProduct(this);

    return purchaseItem;
  }

  public PurchaseItem removePurchaseItem(PurchaseItem purchaseItem) {
    getPurchaseItems().remove(purchaseItem);
    purchaseItem.setProduct(null);

    return purchaseItem;
  }

  public List<Subscription> getSubscription() {
    return subscription;
  }

  public void setSubscription(List<Subscription> param) {
    this.subscription = param;
  }

  public BigDecimal getMargin() {
    return margin;
  }

  public void setMargin(BigDecimal margin) {
    this.margin = margin;
  }

  public boolean isMarginIsPercentage() {
    return marginIsPercentage;
  }

  public void setMarginIsPercentage(boolean marginIsPercentage) {
    this.marginIsPercentage = marginIsPercentage;
  }

  /**
   * Creates a product from a request with its fields.
   * 
   * @return null if there was a validation error; the product if not.
   * @throws ServletException
   * @throws IOException
   */

  public static Product parseFromRequest(HttpServletRequest request, Product p,
      DatabaseHelper dbhelper) throws IOException, ServletException {

    String name = request.getParameter(Utils.PARAM_NAME);
    String description = request.getParameter(Utils.PARAM_DESCRIPTION);
    String category = request.getParameter(Utils.PARAM_CATEGORY);
    String minPriceString = request.getParameter(Utils.PARAM_MIN_PRICE).replaceAll(",", ".");
    String maxPriceString = request.getParameter(Utils.PARAM_MAX_PRICE).replaceAll(",", ".");
    String finalPriceString = request.getParameter(Utils.PARAM_FINAL_PRICE).replaceAll(",", ".");
    String stock = request.getParameter(Utils.PARAM_STOCK);
    BigDecimal minPrice;
    BigDecimal maxPrice;
    BigDecimal finalPrice;
    boolean onSale = false;

    if (!Utils.validateNotEmpty(name)) {
      request.setAttribute("ErrorAddingProduct", "Please fill in the Product Name field");
      return null;
    } else if (!Utils.validateNotEmpty(description)) {
      request.setAttribute("ErrorAddingProduct", "Please add a description of the product");
      return null;
    } else if (!Utils.validateNumber(category, true, true)) {
      request.setAttribute("ErrorAddingProduct", "Please choose a category of the product");
      return null;
    } else if (!Utils.validateNumber(stock, true, true)) {
      request.setAttribute("ErrorAddingProduct",
          "Please fill in correctly the stock of the product");
      return null;
    } else if (!Utils.validateNumber(minPriceString, false, true)) {
      request.setAttribute("ErrorAddingProduct",
          "Please fill in correctly the minimum price of the product");
      return null;
    } else if (!Utils.validateNumber(maxPriceString, false, true)) {
      request.setAttribute("ErrorAddingProduct",
          "Please fill in correctly the maximum price of the product");
      return null;
    } else if (!Utils.validateNumber(maxPriceString, false, true)) {
      request.setAttribute("ErrorAddingProduct",
          "Please fill in correctly the maximum price of the product");
      return null;
    }

    minPrice = new BigDecimal(minPriceString);
    maxPrice = new BigDecimal(maxPriceString);
    finalPrice = new BigDecimal(finalPriceString);

    if (!Utils.validateGreaterOrEqualThan(maxPrice, minPrice)) {
      request.setAttribute("ErrorAddingProduct",
          "The maximum price must be higher than the minimum price");
      return null;
    }

    if (!Utils.validateGreaterOrEqualThan(finalPrice, minPrice)
        || !Utils.validateGreaterOrEqualThan(maxPrice, finalPrice)) {
      request.setAttribute("ErrorAddingProduct",
          "The final price must be between the minimum and maximum prices");
      return null;
    }

    ProductCategory pc = dbhelper.findProductCategoryById(category);
    if (pc == null) {
      request.setAttribute("ErrorAddingProduct", "Please choose an existant product category");
      return null;
    }

    try {
      Part fileMultiPart = request.getPart(Utils.PARAM_IMAGE);
      if (!Utils.validateImageType(fileMultiPart.getSubmittedFileName())) {
        request.setAttribute("ErrorAddingProduct", "The image must be a jpg image");
        return null;
      } else if (!Utils.validateImageSize(fileMultiPart.getSize())) {
        request.setAttribute("ErrorAddingProduct", "The image must have less than 2 GB");
        return null;
      }
      String image = Utils.convertToImage(fileMultiPart);

      p.setImage(image);

    } catch (Exception e) {
    }

    p.setName(name);
    p.setOnSale(onSale);
    p.setDescription(description);
    p.setMaxPrice(maxPrice);
    p.setMinPrice(minPrice);
    p.setFinalPrice(finalPrice);
    // TODO parsear on sale si nos lo pasaran, pero que no sea obligatorio.

    p.setStock(new Integer(stock));
    p.setProductCategory(pc);

    return p;
  }

  public void incrementStock(int quantity) {
    this.stock += quantity;
  }
}
