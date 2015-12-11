package es.tiwuc3m.g01.bank.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

@Entity
public class BankConciliation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String conciliationDate;
	private String supplierCode;
	@Column(name = "AMOUNT", precision = 10, scale = 2)
	private BigDecimal amount;

	public BankConciliation() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConciliationDate() {
		return conciliationDate;
	}

	public void setConciliationDate(String conciliationDate) {
		this.conciliationDate = conciliationDate;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
