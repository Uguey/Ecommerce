package es.uc3m.tiw.g01.model.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Utils {

  public static final String PARAM_ACTION = "action";
  // Client and Supplier
  public static final String PARAM_COMPANY_NAME = "company";
  public static final String PARAM_ID = "id";
  public static final String PARAM_EMAIL = "email";
  public static final String PARAM_NAME = "name";
  public static final String PARAM_SURNAME = "surname";
  public static final String PARAM_PASSWORD = "password";
  public static final String PARAM_PASSWORD_CONFIRMATION = "password_confirmation";
  // Product
  public static final String PARAM_DESCRIPTION = "description";
  public static final String PARAM_CATEGORY = "category";
  public static final String PARAM_CATEGORY_OP = "category_op";
  public static final String PARAM_PRICE = "price";
  public static final String PARAM_PRICE_OP = "price_op";
  public static final String PARAM_MIN_PRICE = "min_price";
  public static final String PARAM_MAX_PRICE = "max_price";
  public static final String PARAM_FINAL_PRICE = "final_price";
  public static final String PARAM_IMAGE = "image";
  public static final String PARAM_ON_SALE = "on_sale";
  public static final String PARAM_STOCK = "stock";
  public static final String PARAM_SUPPLIER = "supplier";
  public static final String PARAM_SUPPLIER_OP = "supplier_op";
  public static final String PARAM_MARGIN_TYPE = "margin_type";
  public static final String PARAM_MARGIN = "margin";

  /**
   * This function hashes a string using SHA-256 Algorithm.
   * 
   * @param password
   * @return
   */
  public static String hashPassword(String password) {

    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(password.getBytes());
      String s = new String(messageDigest.digest(), "UTF-8");
      return new String(Base64.encode(s.getBytes()));

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean validateEmail(String email) {
    return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
  }

  public static boolean validateNumber(String str, boolean longOnly, boolean positiveOnly) {
    try {
      if (longOnly) {
        long i = Long.parseLong(str);
        return !positiveOnly || i > 0;
      } else {
        double d = Double.parseDouble(str);
        return !positiveOnly || d > 0;
      }
    } catch (Exception nfe) {
      return false;
    }
  }

  public static boolean validateSamePassword(String pass1, String pass2) {
    return pass1.equals(pass2);
  }

  public static boolean validateNotEmpty(String str) {
    if (str != null && !str.isEmpty())
      return true;
    return false;
  }

  public static boolean validateGreaterOrEqualThan(BigDecimal greater, BigDecimal smaller) {
    if ((greater != null) && (smaller != null)) {
      return greater.compareTo(smaller) >= 0;
    } else {
      return false;
    }
  }

  public static boolean validateImageSize(long size) {
    if (size > Integer.MAX_VALUE)
      return false;
    else
      return true;
  }

  public static boolean validateImageType(String name) {
    if (name.lastIndexOf(".") >= 0) {
      String extension = name.substring(name.lastIndexOf("."));
      if ((extension.equalsIgnoreCase(".jpg")) || (extension.equalsIgnoreCase(".jpeg"))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Turn a MultiPart File into a String in Base64
   */
  public static String convertToImage(Part fileMultiPart) throws IOException {
    int sizeFile = (int) fileMultiPart.getSize();
    byte[] fileBytes = new byte[sizeFile];
    InputStream inputStream = fileMultiPart.getInputStream();
    while (inputStream.read(fileBytes, 0, sizeFile) != -1);
    String fileBase64 = DatatypeConverter.printBase64Binary(fileBytes);
    String file = "data:image/jpeg;base64, " + fileBase64;
    fileBase64 = null;
    return file;
  }

  /**
   * Compares a String (value) against a list of valid literals (literals)
   */
  public static boolean validateEnum(String[] literals, String value) {
    if (literals.length == 0 || value == null) {
      return false;
    }

    for (String literal : literals) {
      if (value.equals(literal)) {
        return true;
      }
    }

    return false;
  }
}
