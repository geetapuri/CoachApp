package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

public class InvoiceHeaderMapper implements RowMapper {
	
	private static Logger logger = LogManager.getLogger(FeeMgmtMapper.class);


	@Override
	public InvoiceHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
		//logger.info("in InvoiceHeadertMapper now");
		
		InvoiceHeader invoiceHeader = new InvoiceHeader();
		invoiceHeader.setInvoiceAmount(rs.getString("InvoiceAmount"));
		invoiceHeader.setInvoiceDate(rs.getString("InvoiceDate"));
		invoiceHeader.setInvoiceDue(rs.getString("InvoiceDue"));
		invoiceHeader.setInvoiceID(rs.getString("InvoiceID"));
		invoiceHeader.setKidID(rs.getString("KidID"));
		invoiceHeader.setPresentCounter(rs.getString("PresentCounter"));
		
		//logger.info("invoice id = " + invoiceHeader.getInvoiceID());
		
		return invoiceHeader;
		
	}

	

}
