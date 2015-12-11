package es.uc3m.tiw.g01.model.utils;

import java.math.BigDecimal;

public class SalesTuple {
  public BigDecimal adminSales;
  public BigDecimal supplierSales;
  public int year;
  public int month;

  public BigDecimal getAdminSales() {
    return adminSales;
  }

  public BigDecimal getSupplierSales() {
    return supplierSales;
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public SalesTuple(BigDecimal adminSales, BigDecimal supplierSales, int year, int month) {
    this.adminSales = adminSales;
    this.supplierSales = supplierSales;
    this.year = year;
    this.month = month;
  }
}
