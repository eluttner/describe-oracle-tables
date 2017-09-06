package edu.colorado.oda.ora;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;

public class OracleTables {
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);  
	}


	public static void main(String args[]) throws FileNotFoundException {

		String url = "jdbc:oracle:thin:@123.123.123.123:1521:xe";
		Connection con;
		String query = "select COLUMN_NAME, DATA_TYPE, DATA_LENGTH, NULLABLE from USER_TAB_COLUMNS where TABLE_NAME='XXX' order by column_id";
		Statement stmt;
		int padding = 30;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: "); 
			System.err.println(e.getMessage());
		}

		try {

			try(  PrintWriter out = new PrintWriter( "tables.txt" )  ){

				con = DriverManager.getConnection(url, "oda", "oda_pwd");

				stmt = con.createStatement();              

				ResultSet rs;
				ResultSetMetaData rsmd;


				String[] tables = { 
						"cu_adm_main_final", 
						"ps_d_admit_type", 
						"ps_d_class", 
						"ps_d_person", 
						"ps_d_term", 
						"ps_f_adm_appl_eval", 
						"ps_f_adm_appl_stat", 
						"ps_f_adm_funnel", 
						"ps_f_class_enrlmt", 
						"ps_f_class_instrct", 
						"ps_f_term_enrlmt", 
						"ps_r_adm_recrtr", 
						"era_d_at_award", 
						"era_d_at_awdinc", 
						"era_d_ft_account", 
						"era_d_pt_award", 
						"era_d_pt_proposal", 
						"era_d_pt_subcontract", 
						"era_f_activity", 
						"era_f_at_status_history", 
						"era_f_budget_award", 
						"era_f_budget_proposal", 
						"era_f_ft_status_history", 
						"era_f_pt_agreement", 
						"era_f_pt_approval", 
						"era_f_pt_awd_stat", 
						"era_f_pt_deliverable", 
						"era_f_pt_prop_stat", 
				"era_f_pt_terms_cond" };   
				for (String table: tables) { 
					String query_table = query.replace("XXX", table.toUpperCase());

					rs = stmt.executeQuery(query_table);
					rsmd = rs.getMetaData();

					//PrintColumnTypes.printColTypes(rsmd);
					out.println("======================================");
					out.println("== TABLE: " + table);
					out.println("======================================");
					int numberOfColumns = rsmd.getColumnCount();

					for (int i = 1; i <= numberOfColumns; i++) {
						//if (i > 1) System.out.print(",  ");
						String columnName = rsmd.getColumnName(i);
						out.print(padRight(columnName, padding));
					}
					out.println("\n--------------------------------------------------------------------------------------------------------------------------");

					while (rs.next()) {
						for (int i = 1; i <= numberOfColumns; i++) {
							//	if (i > 1) System.out.print(",  ");
							String columnValue = rs.getString(i);
							out.print(padRight(columnValue, padding));
						}
						out.println("");  
					}
					out.println("");  
					out.println("");  
				}
			}
			stmt.close();
			con.close();
		} catch(SQLException ex) {
			System.err.print("SQLException: ");
			System.err.println(ex.getMessage());
		}  
	}
}

class PrintColumnTypes  {

	public static void printColTypes(ResultSetMetaData rsmd)
			throws SQLException {
		int columns = rsmd.getColumnCount();
		for (int i = 1; i <= columns; i++) {
			int jdbcType = rsmd.getColumnType(i);
			String name = rsmd.getColumnTypeName(i);
			System.out.print("Column " + i + " is JDBC type " + jdbcType);
			System.out.println(", which the DBMS calls " + name);
		}
	}
}
