package es.uc3m.tiw.g01.orders;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class OderManager
 */
@Stateless
@LocalBean
public class OderManager implements OrderManagerLocal {
	@Override
    public String generateOrderCode() {
        String s = "ORDER";
        s += new SimpleDateFormat("yyyyMMddhhssSSSSa").format(new Date());
        return s;
      }
}
