package es.uc3m.tiw.g01.bank.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import es.tiwuc3m.g01.bank.model.utils.DatabaseHelper;
import es.uc3m.tiw.g01.bank.responses.PaymentResponse;

@Path("payment")
@Stateless
public class PaymentService {
	
	private final String CREDIT_CARD_PATTERN = "^[A-B][A-Za-z0-9]{19}$";
	
	private int RESULT_CODE_OK = 1;
	private int RESULT_CODE_INVALID_CREDIT_CARD = 2;
	private int RESULT_CODE_MISSING_PARAMS = 3;
	private int RESULT_CODE_UNKNOWN = 4;
	
	@PersistenceContext(unitName = "tiwbanco")
	private EntityManager em;
	private DatabaseHelper dbhelper;

	@GET
	// http://localhost:8080/BancoWEB/payment/perform?creditcard=AA00000000000000&ordercode=0&amount=0&couponcode=0
	@Path("perform")
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentResponse performPayment(
			@QueryParam("creditcard") String creditCard,
			@QueryParam("amount") BigDecimal amount,
			@QueryParam("ordercode") String orderCode,
			@QueryParam("couponcode") String couponCode) {
		PaymentResponse response = new PaymentResponse();
		if (creditCard == null || amount == null | orderCode == null) {
			response.setResult(RESULT_CODE_MISSING_PARAMS);
		} else if (validateCreditCard(creditCard) == false) {
			response.setResult(RESULT_CODE_INVALID_CREDIT_CARD);
		} else {
			try {
				String transactionCode = generateTransactionCode();
				dbhelper = new DatabaseHelper(em);
				dbhelper.createOrder(couponCode, orderCode, transactionCode,
						amount, creditCard);
				response.setAmount(amount);
				response.setCreditCard(creditCard);
				response.setResult(RESULT_CODE_OK);
				response.setTransactionCode(transactionCode);
				response.setCouponCode(couponCode);
				response.setOrderCode(orderCode);
			} catch (Exception e) {
				e.printStackTrace();
				response.setResult(RESULT_CODE_UNKNOWN);
			}
		}
		return response;
	}

	private String generateTransactionCode() {
		String s = "BANCO";
		s += new SimpleDateFormat("yyyyMMddhhssSSSSa").format(new Date());
		return s;
	}

	private boolean validateCreditCard(String creditCard) {
		Pattern pattern = Pattern.compile(CREDIT_CARD_PATTERN);
		Matcher matcher = pattern.matcher(creditCard);
		return matcher.matches();
	}
}