package es.uc3m.tiw.g01.bank.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import es.tiwuc3m.g01.bank.model.utils.DatabaseHelper;
import es.uc3m.tiw.g01.bank.requests.ConciliationRequest;
import es.uc3m.tiw.g01.bank.responses.ConciliationResponse;

@Path("conciliation")
@Stateless
public class ConciliationService {

	@PersistenceContext(unitName = "tiwbanco")
	private EntityManager em;

	private DatabaseHelper dbhelper;

	@POST
	@Path("company")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ConciliationResponse performCompanyConciliation(
			ConciliationRequest request) {

		ConciliationResponse response = new ConciliationResponse();

		if (request.getMonth() == null || request.getYear() == null
				|| request.getAmount() == null) {
			response.setResult(ConciliationResponse.RESULT_CODE_MISSING_PARAMS);
		} else if (request.getMonth() < 1 || request.getMonth() > 12
				|| request.getYear() < 1000 || request.getYear() > 9999
				|| request.getAmount().doubleValue() < 0) {
			response.setResult(ConciliationResponse.RESULT_CODE_WRONG_PARAMS);
		} else {
			try {
				request.setAmount(request.getAmount().multiply(
						new BigDecimal(0.99)));
				dbhelper = new DatabaseHelper(em);
				dbhelper.createCompanyConciliation(new SimpleDateFormat(
						"yyyyMMdd").format(new Date()), request.getAmount());
				response.setResult(ConciliationResponse.RESULT_CODE_OK);
				response.setAmount(request.getAmount().setScale(4, RoundingMode.HALF_UP));
			} catch (Exception e) {
				e.printStackTrace();
				response.setResult(ConciliationResponse.RESULT_CODE_UNKNOWN);
			}
		}
		return response;
	}

	@POST
	@Path("bank")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ConciliationResponse performBankConciliation(
			ConciliationRequest request) {

		ConciliationResponse response = new ConciliationResponse();

		if (request.getMonth() == null || request.getYear() == null
				|| request.getAmount() == null
				|| request.getSupplierCode() == null) {
			response.setResult(ConciliationResponse.RESULT_CODE_MISSING_PARAMS);
		} else if (request.getMonth() < 1 || request.getMonth() > 12
				|| request.getYear() < 1000 || request.getYear() > 9999
				|| request.getAmount().doubleValue() < 0) {
			response.setResult(ConciliationResponse.RESULT_CODE_WRONG_PARAMS);
		} else {
			try {
				dbhelper = new DatabaseHelper(em);
				dbhelper.createBankConciliation(
						new SimpleDateFormat("yyyyMMdd").format(new Date()),
						request.getAmount(), request.getSupplierCode());
				response.setResult(ConciliationResponse.RESULT_CODE_OK);
				response.setAmount(request.getAmount().setScale(4, RoundingMode.HALF_UP));
			} catch (Exception e) {
				e.printStackTrace();
				response.setResult(ConciliationResponse.RESULT_CODE_UNKNOWN);
			}

		}
		return response;
	}
}