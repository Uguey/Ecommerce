package es.uc3m.tiw.g01.bank.responses;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ConciliationResponse implements Serializable{
	
	public static final int RESULT_CODE_OK = 1;
	public static final int RESULT_CODE_WRONG_PARAMS = 2;
	public static final int RESULT_CODE_MISSING_PARAMS = 3;
	public static final int RESULT_CODE_UNKNOWN = 4;
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal fundedAmount;
	private int result;
	
	public ConciliationResponse() {
	}

	public BigDecimal getAmount() {
		return fundedAmount;
	}

	public void setAmount(BigDecimal amount) {
		this.fundedAmount = amount;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}