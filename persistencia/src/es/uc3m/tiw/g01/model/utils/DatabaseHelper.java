package es.uc3m.tiw.g01.model.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import es.uc3m.tiw.g01.model.daos.ClientDAO;
import es.uc3m.tiw.g01.model.daos.ProductCategoryDAO;
import es.uc3m.tiw.g01.model.daos.ProductDAO;
import es.uc3m.tiw.g01.model.daos.PurchaseDAO;
import es.uc3m.tiw.g01.model.daos.SettingDAO;
import es.uc3m.tiw.g01.model.daos.SubscriptionDAO;
import es.uc3m.tiw.g01.model.daos.SupplierDAO;
import es.uc3m.tiw.g01.model.entities.Client;
import es.uc3m.tiw.g01.model.entities.Coupon;
import es.uc3m.tiw.g01.model.entities.Product;
import es.uc3m.tiw.g01.model.entities.ProductCategory;
import es.uc3m.tiw.g01.model.entities.Purchase;
import es.uc3m.tiw.g01.model.entities.PurchaseItem;
import es.uc3m.tiw.g01.model.entities.Setting;
import es.uc3m.tiw.g01.model.entities.Subscription;
import es.uc3m.tiw.g01.model.entities.Supplier;

public class DatabaseHelper implements ClientDAO, ProductDAO, SupplierDAO, ProductCategoryDAO,
    PurchaseDAO, SubscriptionDAO, SettingDAO {

  public static final String PERSISTENCE_UNIT_NAME = "persistencia";

  // purchase query order constants
  public static final int ORDER_BY_DATE_ASC = 1;
  public static final int ORDER_BY_DATE_DESC = 2;
  public static final int ORDER_BY_DEFAULT = 0;

  private EntityManager em;
  private UserTransaction ut;

  public DatabaseHelper(EntityManager em, UserTransaction ut) {
    this.em = em;
    this.ut = ut;

    // Bootstrap initial data. Doesn't do anything if called more than once.
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**************************************************/
  /******************** CLIENT **********************/
  /**************************************************/

  /*
   * (non-Javadoc)
   * 
   * @see controller.ClientsDAO#createClient(java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public boolean createClient(Client newClient) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    Query query = em.createQuery("SELECT c From Client c Where c.email = :email");
    query.setParameter("email", newClient.getEmail());
    List<Client> clients = query.getResultList();
    if (clients.isEmpty()) {
      ut.begin();
      // update database
      em.persist(newClient);
      ut.commit();
      return true;
    } else
      return false;
  }

  @Override
  public Client createClient(String name, String surname, String email, String hashedPass,
      String pwdrecId) throws NotSupportedException, SystemException, SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

    ut.begin();

    Client c = new Client();
    c.setName(name);
    c.setSurname(surname);
    c.setEmail(email);
    c.setHashPwd(hashedPass);
    c.setPwdRecoveryId(new BigInteger(pwdrecId));

    em.persist(c);

    // update database
    ut.commit();

    return c;
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ClientsDAO#removeClientById(int)
   */
  @Override
  public void removeClientById(int id) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    Client c = em.find(Client.class, id);
    ut.begin();
    if (c != null) {
      // if the client has a shopping cart open, we first revert it.
      Purchase p = getShoppingCartPurchase(c);
      if (p != null) {
        // updating products stock
        for (PurchaseItem item : p.getPurchaseItems()) {
          Product prod = em.find(Product.class, item.getProduct().getId());
          prod.incrementStock(item.getQuantity());
          em.merge(prod);
        }
        em.remove(em.merge(p));
      }
    }
    em.remove(em.merge(c));
    ut.commit();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ClientsDAO#updateClientById(int, java.lang.String)
   */
  @Override
  public void updateClientById(int id, String name, String surname, String email, String address)
      throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException {

    ut.begin();

    // get the client
    Client c = em.find(Client.class, id);

    // modify client fields
    if (name != null)
      c.setName(name);
    if (surname != null)
      c.setSurname(surname);
    if (email != null)
      c.setEmail(email);
    if (address != null) {
      c.setAddress(address);
    }

    em.merge(c);
    // update database
    ut.commit();
  }

  @Override
  public void updateClientById(int id, Client modifiedFields) throws SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException {
    updateClientById(id, modifiedFields.getName(), modifiedFields.getSurname(),
        modifiedFields.getEmail(), modifiedFields.getAddress());
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ClientsDAO#findAllClients()
   */
  @Override
  public List<Client> findAllClients() {
    Query q = em.createNamedQuery("Client.findAll");
    return q.getResultList();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ClientsDAO#findClientById(int)
   */
  @Override
  public Client findClientById(int id) {
    return em.find(Client.class, id);
  }

  public Client findAuthenticatedClient(HttpServletRequest request) {
    HttpSession session = request.getSession();
    String email = (String) session.getAttribute("email");
    return findClientByEmail(email);
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ClientsDAO#findClientsByEmail(java.lang.String)
   */
  @Override
  public Client findClientByEmail(String email) {
    Query q = em.createQuery("SELECT c from Client c where c.email = ?1");
    q.setParameter(1, email);
    List<Client> result = (List<Client>) q.getResultList();
    if (result.isEmpty()) {
      return null;
    } else {
      return result.get(0);
    }
  }

  @Override
  public boolean shoppingCartContainsProduct(Client c, Product p) throws SecurityException,
      IllegalStateException, NotSupportedException, SystemException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {

    ArrayList<Product> shoppingCartProducts = getShoppingCart(c);
    for (Product item : shoppingCartProducts) {
      if (item.getId() == p.getId()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int addToShoppingCart(Client c, Product p, int quantity) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {
    // Get shopping cart
    Purchase purchase = getShoppingCartPurchase(c);

    ut.begin();

    // Update stock in product
    int stockToReduce = Math.min(quantity, p.getStock());
    p.setStock(p.getStock() - stockToReduce);
    em.merge(p);

    boolean repeated = shoppingCartContainsProduct(c, p);
    // Update quantity in existing product item
    if (repeated) {
      List<PurchaseItem> piS = purchase.getPurchaseItems();

      for (PurchaseItem purchaseItem : piS) {
        if (purchaseItem.getProduct().getId() == p.getId()) {
          purchaseItem.setQuantity(purchaseItem.getQuantity() + stockToReduce);
          em.merge(purchaseItem);
          break;
        }
      }
    }
    // Or create a new one
    else {
      PurchaseItem pi = new PurchaseItem();
      pi.setProduct(p);
      pi.setPurchas(purchase);
      pi.setQuantity(stockToReduce);
      pi.setReconciled(false);
      em.persist(pi);
    }

    // Update product in database
    ut.commit();

    em.getEntityManagerFactory().getCache().evict(Purchase.class);

    return stockToReduce;
  }

  @Override
  public int removeFromShoppingCart(Client c, Product p, int quantity)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException {

    Purchase purchase = getShoppingCartPurchase(c);

    // get purchase item
    Query q =
        em.createQuery("SELECT pi FROM PurchaseItem pi " + "WHERE pi.product.id = :productId "
            + "AND pi.purchas.id = :purchaseId");
    q.setParameter("productId", p.getId());
    q.setParameter("purchaseId", purchase.getId());

    List<PurchaseItem> purchaseItemList = q.getResultList();

    if (purchaseItemList.isEmpty()) {
      throw new SystemException("Product not present in the shopping cart of the given client");
    }

    PurchaseItem pi = purchaseItemList.get(0);
    int currentQuantityInCart = pi.getQuantity();
    quantity = quantity >= 0 ? quantity : currentQuantityInCart;

    ut.begin();

    if (quantity >= currentQuantityInCart) {
      // update stock
      p.setStock(p.getStock() + pi.getQuantity());
      em.merge(p);

      // no more units of this product, delete this purchaseItem
      em.remove(em.merge(pi));
      em.getEntityManagerFactory().getCache().evict(Purchase.class);

      ut.commit();
      return 0;

    } else {
      // just reduce quantity of this product in purchaseItem
      pi.setQuantity(pi.getQuantity() - quantity);
      em.merge(pi);
      // update stock
      p.setStock(p.getStock() + quantity);
      em.merge(p);

      em.getEntityManagerFactory().getCache().evict(Purchase.class);

      ut.commit();
      return pi.getQuantity();
    }

  }

  @Override
  public ArrayList<Product> getShoppingCart(Client c) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {

    return getProductsFromPurchase(getShoppingCartPurchase(c));
  }

  @Override
  public int numItemsInShoppingCart(Client c) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {

    List<PurchaseItem> itemsInCart = getShoppingCartPurchase(c).getPurchaseItems();
    int numItems = 0;
    for (PurchaseItem pi : itemsInCart) {
      numItems += pi.getQuantity();
    }

    return numItems;
  }

  /**************************************************/
  /******************* SUPPLIER *********************/
  /**************************************************/

  /*
   * (non-Javadoc)
   * 
   * @see controller.SupplierDAO#createSupplier(java.lang.String, java.lang.String,
   * java.lang.String, java.lang.String)
   */

  @Override
  public Supplier createSupplier(String email, String hashedPass, String companyName,
      String pwdrecId) throws NotSupportedException, SystemException, SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

    ut.begin();

    Supplier s = new Supplier();
    s.setEmail(email);
    s.setCompanyName(companyName);
    s.setHashPwd(hashedPass);
    s.setPwdRecoveryId(new BigInteger(pwdrecId));

    em.persist(s);

    // update database
    ut.commit();

    return s;
  }

  @Override
  public boolean createSupplier(Supplier s) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {

    Query query = em.createQuery("SELECT s From Supplier s Where s.email = :email");
    query.setParameter("email", s.getEmail());
    List<Supplier> suppliers = query.getResultList();
    if (suppliers.isEmpty()) {
      ut.begin();
      em.persist(s);
      ut.commit();
      return true;
    } else
      return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.SupplierDAO#removeSupplierById(int)
   */
  @Override
  public void removeSupplierById(int id) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    Supplier s = em.find(Supplier.class, id);
    ut.begin();
    em.remove(em.merge(s));
    em.getEntityManagerFactory().getCache().evictAll();
    ut.commit();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.SupplierDAO#updateSupplierById(int, java.lang.String, java.lang.String)
   */
  @Override
  public void updateSupplierById(int id, String email, String companyName, String hashedPass)
      throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException {

    ut.begin();

    // get the supplier
    Supplier s = em.find(Supplier.class, id);

    // modify supplier fields
    if (email != null)
      s.setEmail(email);
    if (companyName != null)
      s.setCompanyName(companyName);
    if (hashedPass != null)
      s.setHashPwd(hashedPass);

    em.persist(s);

    // update database
    ut.commit();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.SupplierDAO#findAllSuppliers()
   */
  @Override
  public List<Supplier> findAllSuppliers() {
    Query q = em.createNamedQuery("Supplier.findAll");
    return q.getResultList();
  }

  public Supplier findAuthenticatedSupplier(HttpServletRequest request) {
    HttpSession session = request.getSession();
    String email = (String) session.getAttribute("email");
    return findSupplierByEmail(email);
  }

  public Supplier findSupplierById(int id) throws NotSupportedException, SystemException {
    ut.begin();
    return em.find(Supplier.class, id);
  }

  public Supplier findSupplierByEmail(String email) {
    Query q = em.createQuery("SELECT s from Supplier s where s.email = ?1");
    q.setParameter(1, email);
    List<Supplier> result = q.getResultList();
    if (result.isEmpty()) {
      return null;
    } else {
      return result.get(0);
    }
  }

  public void addToCatalog(Supplier s, Product p) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    ut.begin();
    p.setSupplier(s);
    em.persist(p);
    ut.commit();
  }

  /**************************************************/
  /******************* PRODUCT **********************/
  /**************************************************/

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#createProduct(java.lang.String, java.lang.String, java.lang.String,
   * java.lang.String, java.lang.String, models.ProductCategory, models.Supplier, boolean)
   */

  @Override
  public Product createProduct(String name, String description, String finalPrice, String maxPrice,
      String minPrice, String stock, ProductCategory pc, Supplier s, Boolean onSale, String image)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException {

    Product p = new Product();
    p.setDescription(description);
    p.setOnSale(onSale);
    p.setMaxPrice(new BigDecimal(maxPrice.replaceAll(",", ".")));
    p.setMinPrice(new BigDecimal(minPrice.replaceAll(",", ".")));
    p.setFinalPrice(new BigDecimal(finalPrice.replaceAll(",", ".")));
    p.setStock(Integer.parseInt(stock));
    p.setProductCategory(pc);
    p.setSupplier(s);
    p.setName(name);
    p.setImage(image);
    p.setMargin(new BigDecimal(0.01));
    p.setMarginIsPercentage(false);

    ut.begin();

    em.persist(p);
    // invalidates previous cached supplier instances.
    em.getEntityManagerFactory().getCache().evict(Supplier.class);

    // update database
    ut.commit();

    return p;
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#removeProductById(String)
   */
  @Override
  public void removeProductById(String idS) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {

    Product p = findProductById(idS);
    ut.begin();

    em.remove(em.merge(p));
    // invalidate previous cached references of suppliers.
    em.getEntityManagerFactory().getCache().evictAll();
    ut.commit();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#removeProductById(int)
   */
  @Override
  public void removeProductById(int id) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {

    Product p = findProductById(id);
    ut.begin();

    em.remove(em.merge(p));
    // invalidate previous cached references of suppliers.
    em.getEntityManagerFactory().getCache().evictAll();
    ut.commit();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#updateProductById(int, java.lang.String, java.lang.String,
   * java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal, java.lang.Boolean,
   * java.lang.Integer)
   */
  @Override
  public Product updateProductById(int id, String name, String description, BigDecimal maxPrice,
      BigDecimal minPrice, BigDecimal finalPrice, Boolean onSale, Integer stock,
      ProductCategory pc, BigDecimal margin, Boolean marginIsPercentage) throws SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException {

    ut.begin();

    // get the product
    Product p = em.find(Product.class, id);

    // modify product fields
    if (name != null)
      p.setName(name);
    if (maxPrice != null)
      p.setMaxPrice(maxPrice);
    if (minPrice != null)
      p.setMinPrice(minPrice);
    if (finalPrice != null)
      p.setFinalPrice(finalPrice);
    if (description != null)
      p.setDescription(description);
    if (onSale != null)
      p.setOnSale(onSale);
    if (stock != null)
      p.setStock(stock);
    if (pc != null)
      p.setProductCategory(pc);
    if (margin != null)
      p.setMargin(margin);
    if (marginIsPercentage != null)
      p.setMarginIsPercentage(marginIsPercentage);

    em.persist(p);

    // update database
    ut.commit();

    return p;
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#findAllProducts()
   */
  @Override
  public List<Product> findAllProducts() {
    return em.createQuery("SELECT p from Product p").getResultList();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#findProductById(String)
   */
  @Override
  public Product findProductById(String sId) throws NotSupportedException, SystemException {

    return findEntityById(Product.class, sId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#findProductById(int)
   */
  @Override
  public Product findProductById(int id) throws NotSupportedException, SystemException {

    return em.find(Product.class, id);
  }

  @Override
  public List<Product> findProductsOnSale() {
    Query q =
        em.createQuery("SELECT p from Product p " + "WHERE p.onSale = :onsale " + "AND p.stock > 0");
    q.setParameter("onsale", true);
    return (List<Product>) q.getResultList();
  }

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductDAO#findProductsByCategory(models.ProductCategory)
   */
  @Override
  public List<Product> findProductsByCategory(ProductCategory pc) {
    Query q = em.createQuery("SELECT p from Product p where p.productCategory.id = ?1");
    q.setParameter(1, pc.getId());
    return q.getResultList();
  }

  @Override
  public List<Product> findAvailableProductsByCategory(ProductCategory pc) {
    Query q =
        em.createQuery("SELECT p from Product p " + "WHERE p.productCategory.id = ?1 "
            + "AND p.stock > 0");
    q.setParameter(1, pc.getId());
    return q.getResultList();
  }

  @Override
  public List<Product> findProductsByQuery(String name, String categoryOp, int categoryId,
      String supplierOp, int supplierId, String priceOp, BigDecimal price) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT p from Product p ");
    queryBuilder.append("WHERE ");
    queryBuilder.append("p.name LIKE :name ");
    if (categoryId > 0) {
      queryBuilder.append(categoryOp);
      queryBuilder.append(" p.productCategory.id = :categoryId ");
    }
    if (supplierId > 0) {
      queryBuilder.append(supplierOp);
      queryBuilder.append(" p.supplier.id = :supplierId ");
    }
    if (!"ANY".equals(priceOp)) {
      queryBuilder.append("AND ");
      queryBuilder.append("((p.onSale = false AND p.finalPrice " + priceOp + " :price) ");
      queryBuilder.append("OR (p.onSale = true AND p.minPrice " + priceOp + " :price)) ");
    }

    Query q = em.createQuery(queryBuilder.toString());
    q.setParameter("name", "%" + name + "%");
    if (categoryId > 0) {
      q.setParameter("categoryId", categoryId);
    }
    if (supplierId > 0) {
      q.setParameter("supplierId", supplierId);
    }
    if (!"ANY".equals(priceOp)) {
      q.setParameter("price", price);
    }

    return q.getResultList();
  }

  @Override
  public void updateProductMarginBySupplier(BigDecimal margin, Boolean marginIsPercentage,
      int supplierId) throws SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException, SystemException, NotSupportedException {

    Query q = em.createQuery("SELECT p from Product p where p.supplier.id = ?1");
    q.setParameter(1, supplierId);
    List<Product> products = q.getResultList();
    ut.begin();
    for (Product p : products) {
      p.setMargin(margin);
      p.setMarginIsPercentage(marginIsPercentage);
      em.merge(p);
    }
    ut.commit();
  }

  @Override
  public void updateProductMargin(BigDecimal margin, Boolean marginIsPercentage)
      throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException {

    Query q = em.createNamedQuery("Product.findAll");
    List<Product> products = q.getResultList();
    ut.begin();
    for (Product p : products) {
      p.setMargin(margin);
      p.setMarginIsPercentage(marginIsPercentage);
      em.merge(p);
    }
    ut.commit();
  }

  /**************************************************/
  /******************* CATEGORY *********************/
  /**************************************************/

  /*
   * (non-Javadoc)
   * 
   * @see controller.ProductCategoryDAO#createProductCategory(java.lang.String)
   */

  @Override
  public ProductCategory createProductCategory(String name, String image) throws SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException {

    ut.begin();

    ProductCategory p = new ProductCategory();
    p.setName(name);
    p.setImage(image);

    em.persist(p);

    // update database
    ut.commit();

    return p;
  }

  /**
   * @return An ProductCategory with the id specified, or null if it doesn't exist in the DB.
   * @throws NumberFormatException if the string does not contain a valid integer.
   */
  @Override
  public ProductCategory findProductCategoryById(String sId) {
    return findEntityById(ProductCategory.class, sId);
  }

  @Override
  public ProductCategory findProductCategoryByID(int id) {
    return findEntityById(ProductCategory.class, id);
  }

  @Override
  public Purchase findPurchaseByID(String sId) {
    return findEntityById(Purchase.class, sId);
  }

  @Override
  public Purchase findPurchaseByID(int id) {
    return findEntityById(Purchase.class, id);
  }

  // TODO a partir de aqui metodos no probados y desordenados

  /**
   * Proxy to {@link EntityManager#find(Class, int)} but which parses the ID from a String.
   * 
   * @return An entity of the Class specified and with the id specified, or null if it doesn't exist
   *         in the DB.
   * @throws NumberFormatException if the string does not contain a valid integer.
   */
  private <T> T findEntityById(Class<T> cls, String sId) throws NumberFormatException {

    if (sId == null || sId.length() == 0) {
      throw new NumberFormatException("ID param is null or empty");
    }

    sId = sId.replaceAll("\t", "");
    sId = sId.replaceAll("\n", "");
    sId = sId.replaceAll(" ", "");
    Integer id = Integer.parseInt(sId);
    return em.find(cls, id);
  }

  /**
   * Proxy to {@link EntityManager#find(Class, int)}
   * 
   * @return An entity of the Class specified and with the id specified, or null if it doesn't exist
   *         in the DB.
   */
  private <T> T findEntityById(Class<T> cls, int id) {
    return em.find(cls, id);
  }

  /**
   * @return the ProductCategory or null if it wasn't found
   */

  @Override
  public ProductCategory findProductCategoryByName(String name) {
    Query q = em.createQuery("SELECT c from ProductCategory c " + "WHERE c.name = :name ");
    q.setParameter("name", name);

    List<ProductCategory> categories = q.getResultList();

    if (categories.isEmpty()) {
      System.err.println("Couldn't find category with name " + name);
      return null;
    } else {
      return categories.get(0);
    }
  }

  @Override
  public void removeProductCategoryById(int id) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateProductCategoryById(int id, String name) throws SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException, SystemException, NotSupportedException {
    // TODO Auto-generated method stub

  }

  @Override
  public List<ProductCategory> findAllProductCategories() {
    return em.createQuery("SELECT c from ProductCategory c").getResultList();
  }

  @Override
  public Product saveNewProduct(Product p) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    ut.begin();
    em.persist(p);
    // invalidates previous cached supplier instances.
    em.getEntityManagerFactory().getCache().evict(Supplier.class);
    ut.commit();
    return p;
  }

  /**************************************************/
  /******************* PURCHASE *********************/
  /**************************************************/

  public Purchase createPurchase(Client c) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    ut.begin();
    Purchase p = new Purchase();
    p.setClient(c);
    p.setBought(false);
    p.setSplitted(false);
    em.persist(p);
    ut.commit();
    // TODO fill with address info
    return p;
  }

  /*
   * (non-Javadoc)
   * 
   * @see es.uc3m.tiw.g01.model.utils.PurchaseDAO#getShoppingCartPurchase(es.uc3m.tiw.g01
   * .model.entities.Client)
   */
  @Override
  public Purchase getShoppingCartPurchase(Client c) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    Query q =
        em.createQuery("SELECT p FROM Purchase p WHERE p.client.id = :clientId and p.bought = :bought");
    q.setParameter("clientId", c.getId());
    q.setParameter("bought", false);

    List<Purchase> result = q.getResultList();

    switch (result.size()) {
      case 0:
        // No Shopping Cart purchase created, creates one
        return createPurchase(c);
      case 1:
        // Shopping Cart purchase found
        return result.get(0);
      default:
        // more than one open purchases
        return null;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see es.uc3m.tiw.g01.model.utils.PurchaseDAO#getPaidPurchases(es.uc3m.tiw
   * .g01.model.entities.Client, int)
   */
  @Override
  public List<Purchase> getPaidPurchases(Client c, int orderCode) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {
    Query q;
    switch (orderCode) {
      case ORDER_BY_DEFAULT:
        q =
            em.createQuery("SELECT p FROM Purchase p WHERE p.client.id = :clientId and p.bought = :bought");
        break;
      case ORDER_BY_DATE_ASC:
        q =
            em.createQuery("SELECT p FROM Purchase p WHERE p.client.id = :clientId and p.bought = :bought ORDER BY p.purchaseDate ASC");
        break;
      case ORDER_BY_DATE_DESC:
        q =
            em.createQuery("SELECT p FROM Purchase p WHERE p.client.id = :clientId and p.bought = :bought ORDER BY p.purchaseDate DESC");
        break;
      default:
        q =
            em.createQuery("SELECT p FROM Purchase p WHERE p.client.id = :clientId and p.bought = :bought");
        break;
    }
    q.setParameter("clientId", c.getId());
    q.setParameter("bought", true);

    return q.getResultList();
  }

  public List<PurchaseItem> getSplittedPurchaseItemsForSupplier(Supplier s) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {
    Query q =
        em.createQuery("SELECT p FROM PurchaseItem p WHERE p.supplierRef = :supplierId and p.purchas.splitted = true");
    q.setParameter("supplierId", s.getId());
    return q.getResultList();
  }

  public List<PurchaseItem> getAllSplittedPurchaseItems() throws NotSupportedException,
      SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    Query q = em.createQuery("SELECT p FROM PurchaseItem p WHERE p.purchas.splitted = true");
    return q.getResultList();
  }

  public List<SalesTuple> getSalesPerMonth(Supplier s) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {

    Query q1 =
        em.createQuery("SELECT "
            + "SUM(p.margin * p.quantity) "
            + "FROM PurchaseItem p "
            + "WHERE p.supplierRef = :supplierId "
            + "   and p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    q1.setParameter("supplierId", s.getId());
    List<BigDecimal> adminSales = q1.getResultList();

    Query q2 =
        em.createQuery("SELECT "
            + "SUM((p.productPrice - p.margin) * p.quantity) "
            + "FROM PurchaseItem p "
            + "WHERE p.supplierRef = :supplierId "
            + "   and p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    q2.setParameter("supplierId", s.getId());
    List<BigDecimal> supplierSales = q2.getResultList();

    Query q3 =
        em.createQuery("SELECT "
            + "FUNC('YEAR', p.purchas.purchaseDate) "
            + "FROM PurchaseItem p "
            + "WHERE p.supplierRef = :supplierId "
            + "   and p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    q3.setParameter("supplierId", s.getId());
    List<Integer> years = q3.getResultList();

    Query q4 =
        em.createQuery("SELECT "
            + "FUNC('MONTH', p.purchas.purchaseDate) "
            + "FROM PurchaseItem p "
            + "WHERE p.supplierRef = :supplierId "
            + "   and p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    q4.setParameter("supplierId", s.getId());
    List<Integer> months = q4.getResultList();

    List<SalesTuple> tuples = new ArrayList<SalesTuple>();

    for (int i = 0; i < adminSales.size(); i++) {
      tuples.add(new SalesTuple(adminSales.get(i), supplierSales.get(i), years.get(i), months
          .get(i)));
    }

    return tuples;
  }

  public List<SalesTuple> getSalesPerMonth() throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {
    Query q1 =
        em.createQuery("SELECT "
            + "SUM(p.margin * p.quantity) "
            + "FROM PurchaseItem p "
            + "WHERE p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    List<BigDecimal> adminSales = q1.getResultList();

    Query q2 =
        em.createQuery("SELECT "
            + "SUM((p.productPrice - p.margin) * p.quantity) "
            + "FROM PurchaseItem p "
            + "WHERE p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    List<BigDecimal> supplierSales = q2.getResultList();

    Query q3 =
        em.createQuery("SELECT "
            + "FUNC('YEAR', p.purchas.purchaseDate) "
            + "FROM PurchaseItem p "
            + "WHERE p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    List<Integer> years = q3.getResultList();

    Query q4 =
        em.createQuery("SELECT "
            + "FUNC('MONTH', p.purchas.purchaseDate) "
            + "FROM PurchaseItem p "
            + "WHERE p.purchas.splitted = true "
            + "GROUP BY FUNC('YEAR', p.purchas.purchaseDate), FUNC('MONTH', p.purchas.purchaseDate)");
    List<Integer> months = q4.getResultList();

    List<SalesTuple> tuples = new ArrayList<SalesTuple>();

    for (int i = 0; i < adminSales.size(); i++) {
      tuples.add(new SalesTuple(adminSales.get(i), supplierSales.get(i), years.get(i), months
          .get(i)));
    }

    return tuples;
  }

  @Override
  public BigDecimal buyItemsFromPurchase(Purchase p) throws NotSupportedException, SystemException,
      SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
      HeuristicRollbackException {

    BigDecimal purchaseTotal = new BigDecimal(0);

    ut.begin();
    for (PurchaseItem pi : p.getPurchaseItems()) {
      Product pr = pi.getProduct();
      pi.setProduct(null);
      pi.setProductName(pr.getName());
      pi.setProductPrice(pr.getRetailPrice());

      // establishing the margin of the product, with the current price without discount
      if (pr.getMargin() == null)
        pr.setMargin(new BigDecimal(0));
      if (pr.isMarginIsPercentage()) {
        pi.setMargin(pr.getMargin().multiply(new BigDecimal(.01)).multiply(pr.getRetailPrice())
            .setScale(2, RoundingMode.CEILING));
        pi.setMarginNeedsDiscount(true);
      } else {
        pi.setMargin(pr.getMargin());
        pi.setMarginNeedsDiscount(false);
      }
      pi.setSupplierCompany(pr.getSupplier().getCompanyName());
      pi.setSupplierRef(pr.getSupplier().getId());
      em.merge(pi);
      // update purchaseTotal value
      purchaseTotal =
          purchaseTotal.add(pi.getProductPrice().multiply(new BigDecimal(pi.getQuantity())));
    }
    ut.commit();

    return purchaseTotal;
  }

  @Override
  public Purchase buyShoppingCart(Client c, String deliveryFullAddress, Coupon coupon)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException {

    Purchase p = getShoppingCartPurchase(c);

    if (p != null) {
      // a. Calculate total price (minus discount) and set items as bought
      BigDecimal totalPrice = buyItemsFromPurchase(p);

      // b. apply coupon if selected
      BigDecimal discount = new BigDecimal(0);
      if (coupon != null) {
        // b1. calculates real discount based on coupon, which can be up to
        // 10% of total value
        discount = coupon.getRedeemableValue(totalPrice);
      }

      // c. close purchase
      p.setTotalPrice(totalPrice.subtract(discount));
      Date date = new Date();
      p.setPurchaseDate(new Timestamp(date.getTime()));
      p.setDeliveryFulladdress(deliveryFullAddress);
      p.setDiscount(discount);
      p.setBought(true);
      ut.begin();
      em.merge(p);
      ut.commit();

      // invalidates cache
      em.getEntityManagerFactory().getCache().evictAll();

      return p;
    } else
      return null;
  }

  /* get shopping cart from purchase */
  /*
   * (non-Javadoc)
   * 
   * @see es.uc3m.tiw.g01.model.utils.PurchaseDAO#getProductsFromPurchase(es.uc3m
   * .tiw.g01.model.entities.Purchase)
   */
  @Override
  public ArrayList<Product> getProductsFromPurchase(Purchase p) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {

    ArrayList<Product> products = new ArrayList<Product>();
    for (PurchaseItem pi : p.getPurchaseItems()) {
      products.add(pi.getProduct());
    }
    return products;
  }

  /**************************************************/
  /***************** SUBSCRIPTIONS ******************/
  /**************************************************/

  public Subscription createSubscription(Client client, Product prod) throws NotSupportedException,
      SystemException, SecurityException, IllegalStateException, RollbackException,
      HeuristicMixedException, HeuristicRollbackException {
    ut.begin();

    Subscription s = new Subscription();
    s.setClient(client);
    s.setProduct(prod);
    em.persist(s);
    // update database
    ut.commit();

    return s;
  }

  public Subscription findSubscriptionByClientAndProduct(int clientId, int productId) {
    Query q =
        em.createQuery("SELECT s FROM Subscription s WHERE s.client.id = :clientId and s.product.id = :productId");
    q.setParameter("clientId", clientId);
    q.setParameter("productId", productId);

    if (q.getResultList().size() == 0) {
      return null;
    }

    return (Subscription) q.getSingleResult();
  }

  public void removeSubscriptionByClientAndProduct(Client client, Product prod)
      throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
      RollbackException, HeuristicMixedException, HeuristicRollbackException {
    Query q =
        em.createQuery("SELECT s FROM Subscription s WHERE s.client.id = :clientId and s.product.id = :productId");
    q.setParameter("clientId", client.getId());
    q.setParameter("productId", prod.getId());
    Subscription s = (Subscription) q.getSingleResult();
    ut.begin();
    em.remove(em.merge(s));
    // invalidate previous cached references of subscriptions.
    em.getEntityManagerFactory().getCache().evictAll();
    ut.commit();
  }

  public List<Subscription> findSubscriptionsByClient(Client client) {
    Query q = em.createQuery("SELECT s FROM Subscription s WHERE s.client.id = :clientId");
    q.setParameter("clientId", client.getId());
    return q.getResultList();
  }

  public List<Subscription> findSubscriptionsBySupplier(Supplier supplier) {
    Query q =
        em.createQuery("SELECT s FROM Subscription s WHERE s.product.supplier.id = :supplierId");
    q.setParameter("supplierId", supplier.getId());
    return q.getResultList();
  }

  /**************************************************/
  /******************** SETTINGS ********************/
  /**************************************************/

  public Setting setUpSettings(String email, String password, int stockThresHoldLow,
      int stockThresHoldVeryLow) throws NotSupportedException, SystemException, SecurityException,
      IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
    Setting set = getSetting();
    if (set != null)
      return null;
    Setting s = new Setting();
    s.setEmail(email);
    s.setPassword(Utils.hashPassword(password));
    s.setStockThresHoldLow(stockThresHoldLow);
    s.setStockThresHoldVeryLow(stockThresHoldVeryLow);
    ut.begin();
    em.persist(s);
    ut.commit();
    return s;
  }

  public Setting getSetting() {
    Query q = em.createQuery("SELECT s FROM Setting s");
    if (q.getResultList().size() == 0)
      return null;
    return (Setting) q.getSingleResult();
  }

  /***************************************************/

  public void init() throws SecurityException, IllegalStateException, NotSupportedException,
      SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

    // fill table just one
    // TODO test purposes, remove.
    if (!findAllProducts().isEmpty() || !findAllSuppliers().isEmpty()
        || !findAllClients().isEmpty()) {
      return;
    }

    // set up settings
    Setting set = setUpSettings("admin@admin.com", "tiw", 10, 5);
    if (set == null) {
      return;
    }

    int NUM_PRODUCTS = 30;
    int NUM_CLIENTS = 5;
    int NUM_SUPPLIERS = 5;

    List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
    List<Product> products = new ArrayList<Product>();
    List<Supplier> suppliers = new ArrayList<Supplier>();

    ProductCategory earthenWarePitcher =
        createProductCategory("earthenware", "images/earthenWarePitcher.png");
    ProductCategory wineGlass = createProductCategory("wineglass", "images/wineGlass.png");
    ProductCategory platter = createProductCategory("platter", "images/platter.png");
    ProductCategory flowerpot = createProductCategory("flowerpot", "images/flowerpot.png");
    ProductCategory clayPot = createProductCategory("claypot", "images/clayPot.png");
    ProductCategory vessel = createProductCategory("vessel", "images/vessel.png");

    productCategories.add(earthenWarePitcher);
    productCategories.add(wineGlass);
    productCategories.add(platter);
    productCategories.add(flowerpot);
    productCategories.add(clayPot);
    productCategories.add(vessel);

    for (int i = 0; i < NUM_SUPPLIERS; i++) {
      suppliers.add(createSupplier("supplier" + i + "@badoo.net", Utils.hashPassword("1234"),
          "Random Brand " + i + ", S.L.", "12"));
    }

    for (int i = 0; i < NUM_PRODUCTS; i++) {

      Supplier currentSupplier = suppliers.get(i % 5);

      int minPrice = 1 + (int) (Math.random() * 10);
      int maxPrice = minPrice * 2;
      int price = (minPrice + maxPrice) / 2;

      int initialStock = (int) (Math.random() * 20);
      Product p =
          createProduct("Ceramic product " + (i + 1),
              "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                  + "Nullam et gravida massa. Aenean sit amet iaculis velit. "
                  + "Integer at mauris finibus, efficitur erat id, congue nunc. "
                  + "Donec hendrerit egestas urna.", String.valueOf(price),
              String.valueOf(maxPrice), String.valueOf(minPrice), String.valueOf(initialStock),
              productCategories.get(i % productCategories.size()), currentSupplier,
              Math.random() < 0.3, "images/botijo.jpeg");
      products.add(p);
    }

    for (int i = 0; i < NUM_CLIENTS; i++) {
      createClient("Anon", "Client", "client" + i + "@badoo.net", Utils.hashPassword("1234"), "12");
    }

  }
}
