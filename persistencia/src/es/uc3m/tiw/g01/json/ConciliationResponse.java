package es.uc3m.tiw.g01.json;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ConciliationResponse implements Serializable{
	
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