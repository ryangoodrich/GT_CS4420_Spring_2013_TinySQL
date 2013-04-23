package com.sqlmagic.tinysql;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class GUIExecuteSQL extends JPanel {

	
	
	JTextArea inputArea, outputArea;
	JButton selectButton, executeButton, insertButton, updateButton, createButton, deleteButton;
	JLabel inputLabel, outputLabel;
	JScrollPane scrollPane;
	//table
	JScrollPane tableScrollPane;
	JTable resultTable;
	String[][] data ={};
	String[] columns = {};
	
	ResultSet display_rs;
	Statement stmt;
	ResultSetMetaData meta;
	StringBuffer lineOut;
	int rsColCount, i;
	FileWriter spoolFileWriter = null;
	String newLine = System.getProperty("line.separator"); 
	boolean isTableSelected = (GUITopLevel.selectedTable == null ? false : true);
	
	
	public GUIExecuteSQL() {
		//set layout
		this.setLayout(null);
		
		//Labels
		inputLabel = new JLabel("Enter SQL");
		inputLabel.setBounds(15, 10, 70, 30);
		this.add(inputLabel);
		outputLabel = new JLabel("Result");
		outputLabel.setBounds(20, 100, 50, 20);
		this.add(outputLabel);
		
		
		//Text Area
		
	    inputArea = new JTextArea();
		inputArea.setLineWrap(true);
		JScrollPane textScroll = new JScrollPane(inputArea);
		textScroll.setBounds(15,35,630,50);
		this.add(textScroll);

		//JTable
		resultTable = new JTable(new DefaultTableModel(data, columns));
		resultTable.setPreferredScrollableViewportSize(new Dimension(450,63));
		resultTable.setFillsViewportHeight(true);
		tableScrollPane = new JScrollPane(resultTable); 
		tableScrollPane.setBounds(15, 120, 630, 300);
		this.add(tableScrollPane);
		
		//Buttons
		selectButton = new JButton("Select");
		selectButton.setBounds(263,10,70,25);
		this.add(selectButton);
		insertButton = new JButton("Insert");
		insertButton.setBounds(336,10,70,25);
		this.add(insertButton);
		updateButton = new JButton("Update");
		updateButton.setBounds(407,10,81,25);
		this.add(updateButton);
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(490,10,81,25);
		this.add(deleteButton);
		executeButton = new JButton("Run SQL");
		executeButton.setBounds(500,88,100,30);
		this.add(executeButton);
		
		
		
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					displaySelect();
				} catch (SQLException e) {
					if(tinySQLGlobals.DEBUG)
						e.printStackTrace();
				}
			}
		});
		
		
		updateButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				try {
					displayUpdate();
				} catch (SQLException e) {
					if(tinySQLGlobals.DEBUG)
						e.printStackTrace();
				}
				
			}
		});
		
		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					displayInsert();
				} catch (SQLException e) {
					if(tinySQLGlobals.DEBUG)
						e.printStackTrace();
				}
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					displayDelete();
				} catch (SQLException e) {
					if(tinySQLGlobals.DEBUG)
						e.printStackTrace();
				}
			}
		});
		
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inputStream = inputArea.getText();
				if(GUITopLevel.con == null){
					
				}
						
					try{
					execute(inputStream);
					} catch (SQLException e) {
					
					JOptionPane.showMessageDialog(null, "Your SQL has some issue");
					if(tinySQLGlobals.DEBUG)
						e.printStackTrace();
					 
				}
			}
		});
	}
	
	
	public Vector<String> getTableColumnNames() throws SQLException{
		Connection tmpCon = GUITopLevel.con;
		ResultSet rs = tmpCon.getMetaData().getColumns(null,null,GUITopLevel.selectedTable,null);
		Vector<String> columns = new Vector<String>();
		while(rs.next()){
			String c = rs.getString("COLUMN_NAME");
			columns.add(c);
		}
		return columns;		
	}

	public Vector<String> getTableColumnTypes() throws SQLException{
		Connection tmpCon = GUITopLevel.con;
		ResultSet rs = tmpCon.getMetaData().getColumns(null,null,GUITopLevel.selectedTable,null);
		int col_count = 0; 
		Vector<String> columns = new Vector<String>();
		while(rs.next()){
			String c = rs.getString(6);
			columns.add(c);
		}
		return columns;		
	}
	
	private void displaySelect() throws SQLException{
		String output;
		String columns = "";
		String tablename = "";
		
		if(GUITopLevel.selectedTable != null){
			Vector<String> col = getTableColumnNames();
			columns = col.firstElement();
			for(int i = 1; i < col.size(); i++){
				columns += ", " + col.get(i);
			}
			tablename = GUITopLevel.selectedTable;
		}
		else{
			columns = "*";
			tablename = "tableName";
		}
		output = "SELECT " + columns + " FROM " + tablename;
		
		inputArea.setText(output);
		
	}
	
	private void displayUpdate() throws SQLException{
		String output;
		String columns;
		String tablename = "";
		
		if(GUITopLevel.selectedTable != null){
			Vector<String> col = getTableColumnNames();
			Vector<String> types = getTableColumnTypes();
			columns = col.firstElement() + "=" + types.firstElement();
			for(int i = 1; i < col.size(); i++){
				columns += ", " + col.get(i) + "=" + types.get(i);
			}
			tablename = GUITopLevel.selectedTable;
		}
		else{
			columns = " ";
			tablename = "tableName";
		}
		
		output = "UPDATE " + tablename + " SET " + columns + " \nWHERE " + columns;
		inputArea.setText(output);
		
	}
	
	
	private void displayInsert() throws SQLException {
		String output;
		String columns;
		String tablename = "";
		String values = "";
		
		if(GUITopLevel.selectedTable != null){
			Vector<String> col;
			Vector<String> types = getTableColumnTypes();
			col = getTableColumnNames();
			columns = col.firstElement();
			values = types.firstElement();
			for(int i = 1; i < col.size(); i++){
				columns += ", " + col.get(i);
				values += ", " + types.get(i);
			}
			tablename = GUITopLevel.selectedTable;
		}
		else{
			columns = " columns ";
			tablename = "tableName";
		}
		
		output = "INSERT INTO " + tablename + "(" + columns + ")" + "\nVALUES (" + values + ")";
		inputArea.setText(output);
	}
	
	private void displayDelete() throws SQLException{
		String output;
		String columns = "";
		String tablename = "";

		if(GUITopLevel.selectedTable != null){
			Vector<String> col = getTableColumnNames();
			Vector<String> types = getTableColumnTypes();
			columns = col.firstElement() + "=" + types.firstElement();
			for(int i = 1; i < col.size(); i++){
				columns += ", " + col.get(i) + "=" + types.get(i);
			}
			tablename = GUITopLevel.selectedTable;
		}
		else{
			columns = " ";
			tablename = "tableName";
		}
		
		output = "DELETE FROM " + tablename +  " \nWHERE " +columns;
		inputArea.setText(output);
	}
	
	public void execute(String inputQuery) throws SQLException{

		stmt = GUITopLevel.con.createStatement();
		 if ( inputQuery.toUpperCase().startsWith("SELECT") ) 
         {
			// start the timer
			Long time = System.nanoTime();
			if (GUITopLevel.cache)
				inputQuery = checkForCache(inputQuery);
            display_rs = stmt.executeQuery(inputQuery);
            String totalTime = ((System.nanoTime() - time) /  1000000) + " ms";
            if ( tinySQLGlobals.DEBUG )
            	System.out.println("Query Time: " + totalTime);
            GUITopLevel.lblTimer.setText(totalTime);
            
            if ( display_rs == null )
            {
               System.out.println("Null ResultSet returned from query");
            }
            
            //setup table
            ResultSet table_rs = stmt.executeQuery(inputQuery);
            displayResults(table_rs);
         }
		 else if ((inputQuery.toUpperCase().startsWith("INSERT")) || (inputQuery.toUpperCase().startsWith("UPDATE")) || (inputQuery.toUpperCase().startsWith("DELETE")) ){
			 try{
				int num = stmt.executeUpdate(inputQuery);
				 JOptionPane.showMessageDialog(null, "Finished Query\n" + inputQuery);	 
			 }
			 catch(SQLException e){
				 JOptionPane.showMessageDialog(getParent(), "Query was unsuccessful");
			 }
		 }
		 else{
			 try{
				 stmt.executeUpdate(inputQuery);
				 JOptionPane.showMessageDialog(null, "Finished Query\n" + inputQuery);	
				 
			 		DatabaseMetaData dbMeta = GUITopLevel.con.getMetaData();
			  		ResultSet tables_rs = dbMeta.getTables(null,null,null,null);
				    Vector tableList = new Vector();
				    String tableName;
				      while ( tables_rs.next() )
				      {
				         tableName = tables_rs.getString("TABLE_NAME");
				         if (tableName.charAt(0) != '_')
				        	 tableList.addElement(tableName);
				      }
			  		GUITopLevel.setTableList(tableList);
			 }
			 catch(SQLException e){
				 JOptionPane.showMessageDialog(getParent(), "Query was unsuccessful");
			 }		 
		}
		
	 }
	
	public void displayResults(ResultSet display_rs) throws SQLException{
		meta = display_rs.getMetaData();
        columns = getColumnName(meta);
        resultTable.setModel(new DefaultTableModel(data, columns));
        
        //Add rows
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
		    int cols = meta.getColumnCount();
		    Object[] temp;
		    while(display_rs.next()){
		       temp = new Object[cols];
			   for (int ii=0; ii<cols; ii++){
				   temp[ii] = display_rs.getString(ii+1);
			   }
			   model.addRow(temp);
		   }
		   
		  resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	   };
	
	   
	   
	   
	public String[] getColumnName(ResultSetMetaData tableMeta ) throws SQLException{
			  int trsColCount = tableMeta.getColumnCount();
	          String[] columnNames = new String[trsColCount];
	          for ( i = 0; i < trsColCount; i++ )
	          {
	             columnNames[i] = tableMeta.getColumnName(i + 1);
	             if (columnNames[i].indexOf("-") > 0)
	          	   columnNames[i] = columnNames[i].substring(columnNames[i].indexOf("-") + 1);
	          }  
	          return columnNames;
	 }

	   
	  private String CACHE_DIVISOR = "-";
	  private String checkForCache(String originalSql) throws SQLException {
		   // get the cache table name
		   String[] result = getCacheTableName(originalSql);
		   String cacheTableName = result[0];
		   String newSql = result[1];
		   
		   // check if the cache has been created by taking the where clause and the from tables
		   ResultSet tables = GUITopLevel.con.getMetaData().getTables(null,null,null,null);
		   while (tables.next()) {
			   if (tables.getString("TABLE_NAME").equals(cacheTableName)) {
				   if ( tinySQLGlobals.DEBUG )
					   System.out.println(newSql);
				   return newSql;
			   }
		   }
		   
		   // no table exists so we need to create one...
		   // lets take out the select stuff... 
		   String tmpSql = "SELECT * " + originalSql.substring(originalSql.toUpperCase().indexOf("FROM")).trim();
		   
		   // first we need to run the sql, so lets do that
		   ResultSet allValues = GUITopLevel.con.createStatement().executeQuery(tmpSql);
		   
		   // create a create table string
		   String createTable = "CREATE TABLE " + cacheTableName + " (";
		   
		   // and the insert table name
		   String insert = "INSERT INTO " + cacheTableName + " (";
		   
		   // now we need to construct a table with the data and column layout of the result
		   ResultSetMetaData allValuesMeta = allValues.getMetaData();
		   int dataSize = allValuesMeta.getColumnCount();
		   for (int i = 1; i <= dataSize; i ++) {
			   // get the name of the column
			   String tableName = allValuesMeta.getTableName(i);
			   String columnName = tableName.substring(tableName.indexOf("->") + 2)  + CACHE_DIVISOR + allValuesMeta.getColumnName(i);
			   
			   // add the column name to the string
			   insert += columnName;
			   
			   // get the type and len of the column
			   String columnType = allValuesMeta.getColumnTypeName(i) + "(" + allValuesMeta.getPrecision(i);
			   // if its a float we need the precision
			   if (allValuesMeta.getColumnTypeName(i).equals("FLOAT"))
				   columnType += allValuesMeta.getScale(i);
			   columnType += ")";
			   
			   // and then put it together and add it to the base string
			   createTable += "\"" + columnName + "\" " + columnType;
			   
			   // add a comma if needed
			   if (i < dataSize) {
				   createTable += ", ";
				   insert += ", ";
			   }
		   }
		   
		   // finish off the ) and run it
		   createTable += ")";
		   if ( tinySQLGlobals.DEBUG )
			   System.out.println(createTable);
		   GUITopLevel.con.createStatement().executeQuery(createTable);
		   
		   // then go through all the results and insert it into the table
		   insert += ") VALUES ";
		   ArrayList<String> insertList = new ArrayList<String>();
		   while (allValues.next()) {
			   String insertEntry = "(\"";
			   for (int i = 1; i <= dataSize; i ++) {
				   String value = allValues.getString(i);
				   if (value != null) {
					   value = value.replaceAll("\\\"", "\\\\\"");
				   }
				   insertEntry += value;
				   
				   if (i < dataSize)
					   insertEntry += "\",\"";
			   }
			   insertEntry += "\")";
			   insertList.add(insertEntry);
		   }
		   
		   for (String insertEntry : insertList) {
			   if ( tinySQLGlobals.DEBUG )
				   System.out.println(insert + insertEntry);
			   GUITopLevel.con.createStatement().executeQuery(insert + insertEntry);
		   }
		   
		   return newSql;
	   }
	   	   
	   private String[] getCacheTableName(String sql) {;
		   // make the whole statment uppercase
		   sql.toUpperCase();
		   
		   // get useful index
		   int len = sql.length();
		   int from = sql.toUpperCase().indexOf("FROM");
		   int where = sql.toUpperCase().indexOf("WHERE");
		   
		   // the new select is from the SELECT statment to the FROM statement
		   String newSQL = sql.substring(0, from).trim();
		   
		   // create a string for the table name
		   String cacheTableName = where > 0 ? "_" : ""; 
		   
		   // get the tables that we are looking at
		   String tables = sql.substring(from + 4, where > 0 ? where : len);
		   for (String table : tables.split(",")) {
			   // trim the table
			   table = table.trim();
			   
			   // check if we have an alisis
			   int space = table.indexOf(" ");
			   if (space > 0) {
				   String key = table.substring(space);
				   table = table.substring(0, space);
				   // replace all the aliasis in the string
				   sql = sql.replaceAll(key + "\\.", " " + table + ".");
				   if ( tinySQLGlobals.DEBUG )
					   System.out.println(sql);
				   newSQL = newSQL.replaceAll(key + "\\.", " " + table + CACHE_DIVISOR);
				   if ( tinySQLGlobals.DEBUG )
					   System.out.println(newSQL);
			   }
			   
			   // and save the talbe to the string
			   cacheTableName += table;
		   }
		   
		   // compress the rest of the string and append it also
		   where = sql.toUpperCase().indexOf("WHERE");
		   if (where > 0) {
			   String tmpName = sql.substring(where + 5).replaceAll(" ", "");
			   tmpName = tmpName.replaceAll("\\)", "");
			   tmpName = tmpName.replaceAll("\\(", "");
			   cacheTableName += tmpName;
		   }
		   
		   newSQL += " FROM " + cacheTableName;
		   
		   // return the name
		   String[] result = new String[2];
		   result[0] = cacheTableName;
		   result[1] = newSQL;
		   return result;
	   }
}
