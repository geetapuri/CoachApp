package com.example.demo;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class FeeMgmtJDBCTemplate implements FeeMgmtDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private static Logger logger = LogManager.getLogger(FeeMgmtJDBCTemplate.class);


	public void setDataSource(DataSource dataSource) {
		//this.dataSource = dataSource;
	    this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	   
	}

	@Override
	public String addFees(List<FeeMgmt> data) {
		// TODO Auto-generated method stub
		
		logger.info("calling addFees method now");
		//TODO batchupdate like mark attendance here
	
		
		String SQL = "UPDATE FEEMGMT  SET FeePaid= ? " +
					" WHERE kidID=? AND  DATEOFATTENDANCE =?";
		
		jdbcTemplateObject.batchUpdate( SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				
				//((List<Integer>) data).get(i);
				ps.setString(1, data.get(i).getFeePaid());
				
				ps.setString(2,  data.get(i).getKidID());
				ps.setString(3, data.get(i).getDateOfAttendance());
				//ps.setInt(5, i+11);
				
				//check if fee is paid, then decrease its present counter in invoice header by 4
				// and set the Invoice Due Counter as N
				if (data.get(i).getFeePaid().equals("Y")) {
						/*String sql3 = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter-4, "
								+ "  InvoiceDue='N' "
								+ " 	WHERE KidID = ?";
						jdbcTemplateObject.update(sql3, data.get(i).getKidID());
					}*/
				String kidID = data.get(i).getKidID();
				String groupID = data.get(i).getGroupID();
				String invoiceDate = data.get(i).getDateOfAttendance();
					
				String sql4 = "SELECT PackageID from GROUPOFKIDS WHERE GroupID = ?";
				
				String packageID = (String)jdbcTemplateObject.queryForObject(
						sql4, new Object[] { groupID }, String.class);
				
				switch (packageID) {
				case "1" :
					updateInvoiceHeaderFourLessonFeePaid(kidID, groupID, invoiceDate  );
					logger.info("in 1");
					break;
				case "2" :
					
					updateInvoiceHeaderMonthFeePaid(kidID, groupID, invoiceDate );
					logger.info("in 2");
					break;
				case "3" :
					updateInvoiceHeaderEveryLessonFeePaid(kidID, groupID, invoiceDate);
					logger.info("in 3");
					break;
					
				}
				
				
				}
				
				
			}
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return data.size();
			}
			
		});
		
		
		return "SUCCESS ";
	}
	
	public void updateInvoiceHeaderFourLessonFeePaid(String kidID, String groupID, String invoiceDate){
		String sql4 = "SELECT InvoiceDue FROM INVOICE_HEADER "
				+ 		" WHERE KidID=? AND GroupID=? ";
		
		String invoiceDue = (String)jdbcTemplateObject.queryForObject(
				sql4, new Object[] { kidID, groupID }, String.class);
		
		String sql5 = "SELECT InvoiceAmount FROM INVOICE_HEADER "
				+ 		" WHERE KidID=? AND GroupID = ?";
		
		String invoiceAmount = (String)jdbcTemplateObject.queryForObject(
				sql5, new Object[] { kidID, groupID }, String.class);
		
		if(invoiceDue.equals("Y")) {
			if (invoiceAmount.equals("100")) {
				String sql3 = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter-4, "
						+ "  InvoiceDue='N' , InvoiceAmount=InvoiceAmount-100"
						+ " 	WHERE KidID = ? AND GroupID=? ";
				jdbcTemplateObject.update(sql3, kidID, groupID);
			} else {
				String sql3 = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter-4, "
						+ "  InvoiceDue='Y' , InvoiceAmount=InvoiceAmount-100"
						+ " 	WHERE KidID = ? AND GroupID=? ";
				jdbcTemplateObject.update(sql3, kidID, groupID);
			}
		} else {
			return;
		}
		
	}
	
	public void updateInvoiceHeaderMonthFeePaid(String kidID, String groupID, String invoiceDate){
		String sql4 = "SELECT InvoiceDue FROM INVOICE_HEADER "
				+ 		" WHERE KidID=? AND GroupID=?";
		
		String invoiceDue = (String)jdbcTemplateObject.queryForObject(
				sql4, new Object[] { kidID, groupID }, String.class);
		
		String sql5 = "SELECT InvoiceAmount FROM INVOICE_HEADER "
				+ 		" WHERE KidID=? AND GroupID = ?";
		
		String invoiceAmount = (String)jdbcTemplateObject.queryForObject(
				sql5, new Object[] { kidID, groupID }, String.class);
		
		if(invoiceDue.equals("Y")) {
			if (invoiceAmount.equals("70")) {
		
				String sql3 = "UPDATE INVOICE_HEADER SET  "
					+ "  InvoiceDue='N', InvoiceAmount= InvoiceAmount-70 "
					+ " 	WHERE KidID = ? AND GroupID=? ";
				jdbcTemplateObject.update(sql3, kidID, groupID);
			} else {
				
				String sql3 = "UPDATE INVOICE_HEADER SET  "
						+ "  InvoiceDue='Y', InvoiceAmount= InvoiceAmount-70 "
						+ " 	WHERE KidID = ? AND GroupID=? ";
					jdbcTemplateObject.update(sql3, kidID, groupID);
			
			}
		} else {
			return;
		}
		
	}
	
	public void updateInvoiceHeaderEveryLessonFeePaid(String kidID, String groupID, String invoiceDate){
		String sql4 = "SELECT InvoiceDue FROM INVOICE_HEADER "
				+ 		" WHERE KidID=? AND GroupID=?";
		
		String invoiceDue = (String)jdbcTemplateObject.queryForObject(
				sql4, new Object[] { kidID, groupID }, String.class);
		
		String sql5 = "SELECT InvoiceAmount FROM INVOICE_HEADER "
				+ 		" WHERE KidID=? AND GroupID = ?";
		
		String invoiceAmount = (String)jdbcTemplateObject.queryForObject(
				sql5, new Object[] { kidID, groupID }, String.class);
		
		if(invoiceDue.equals("Y")) {
				if (invoiceAmount.equals("15")) {
					String sql3 = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter-1, "
							+ "  InvoiceDue='N' , InvoiceAmount=InvoiceAmount-15 "
							+ " 	WHERE KidID = ? AND GroupID=? ";
					jdbcTemplateObject.update(sql3, kidID, groupID);
				} else {
					String sql3 = "UPDATE INVOICE_HEADER SET PresentCounter=PresentCounter-1, "
							+ "  InvoiceDue='Y' , InvoiceAmount=InvoiceAmount-15 "
							+ " 	WHERE KidID = ? AND GroupID=? ";
					jdbcTemplateObject.update(sql3, kidID, groupID);
				
				}
		}
			
		else {
			return;
		}
		
		
		
		
	}
	

	@Override
	public List<FeeMgmt> viewFees(FeeMgmt data) {
		// TODO Auto-generated method stub
		logger.info("inside view Fees");
		
		String SQL = "SELECT * FROM FEEMGMT WHERE KIDID=? AND GROUPID =? ORDER BY "
				+ " DATEOFATTENDANCE DESC" ;
		
	    List <FeeMgmt> feeList = jdbcTemplateObject.query(SQL,new Object[] 
	    		{data.getKidID(), data.getGroupID() },new FeeMgmtMapper());
		
	    return feeList;
	}

	public void updateInvoice() {
		logger.info("inside update invoice");
		
		String sql = "SELECT * FROM INVOICE_HEADER ";
		
		List <InvoiceHeader> invoiceList = jdbcTemplateObject.query(sql, new InvoiceHeaderMapper());
		
		int i=0;
		//logger.info("starting the while loop, size of invoiceList = " + invoiceList.size());
		//int j = invoiceList.size();
		while (i < invoiceList.size()) {
			//logger.info("i = "+ i);
			String presentCounter = invoiceList.get(i).getPresentCounter();
			String kidID = invoiceList.get(i).getKidID();
			//logger.info("PC = "+ presentCounter + "kidID = " + kidID);
			if (presentCounter.equals("4")) {
				String sql2 = "UPDATE INVOICE_HEADER SET InvoiceDue= 'Y' "
						+ " WHERE KidID=?";
				
				int rows = jdbcTemplateObject.update(sql2, invoiceList.get(i).getKidID());
				//logger.info("rows updated = "+ rows);
				
			}
			i++;
			
			
		}
		
		
		
		
	}

	public List<FeeMgmt> viewFeeForGroupDate(FeeMgmt data) {
		
		String sql = " SELECT F.*, K.KIDNAME "
				+ " FROM FEEMGMT F, KID K "
				+ " WHERE F.GroupID= ?"
				+ " AND F.DateOfAttendance = ? "
				+ " AND F.KidID=K.KidID "; 
		
	    List <FeeMgmt> feeMgmt = jdbcTemplateObject.query(sql,new Object[] 
	    		{data.getGroupID(), data.getDateOfAttendance() },new ShowKidsFeeMapper());
	    
	   
		return feeMgmt;
	}

}
