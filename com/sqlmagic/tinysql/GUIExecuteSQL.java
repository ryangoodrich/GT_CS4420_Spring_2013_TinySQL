package com.sqlmagic.tinysql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableColumn;

public class GUIExecuteSQL extends JPanel {

	
	
	JTextArea inputArea, outputArea;
	JButton selectButton, executeButton, insertButton, updateButton, createButton, deleteButton;
	JLabel inputLabel, outputLabel;
	JScrollPane scrollPane;
	JScrollPane scrollPane2;
	JTable resultTable;
	ResultSet display_rs;
	Statement stmt;
	ResultSetMetaData meta;
	StringBuffer lineOut;
	int rsColCount, i;
	FileWriter spoolFileWriter = null;
	String newLine = System.getProperty("line.separator"); 
	boolean isTableSelected = GUITopLevel.selectedTable == null ? false : true;
	
	
	public GUIExecuteSQL() {
		//set layout
		this.setLayout(null);
		
		//Labels
		inputLabel = new JLabel("Enter SQL");
		inputLabel.setBounds(15, 10, 70, 30);
		this.add(inputLabel);
		outputLabel = new JLabel("Result");
		outputLabel.setBounds(15, 130, 50, 20);
		this.add(outputLabel);
		
		
		//Text Area
	    inputArea = new JTextArea();
		inputArea.setLineWrap(true);
		inputArea.setBounds(15, 35, 630, 50);
		this.add(inputArea);
	    outputArea = new JTextArea();
	    outputArea.setEditable(false);
	    scrollPane = new JScrollPane(outputArea);
		scrollPane.setBounds(15, 150, 630, 240);
		//table Pane
		resultTable = new JTable();
		scrollPane2 = new JScrollPane(resultTable);
		scrollPane2.setBounds(15, 400, 630, 30);
		this.add(scrollPane2);
		this.add(scrollPane);
		
		//Buttons
		selectButton = new JButton("Select");
		selectButton.setBounds(250,10,70,25);
		this.add(selectButton);
		insertButton = new JButton("Insert");
		insertButton.setBounds(320,10,70,25);
		this.add(insertButton);
		updateButton = new JButton("Update");
		updateButton.setBounds(390,10,79,25);
		this.add(updateButton);
		executeButton = new JButton("Run SQL");
		executeButton.setBounds(450,100,100,30);
		this.add(executeButton);
		
		
		
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displaySelect();
			}
		});
		
		
		updateButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				displayUpdate();
				
			}
		});
		
		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayInsert();
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
					// TODO Auto-generated catch block
					
					JOptionPane.showMessageDialog(null, "You SQL has some issue");
					e.printStackTrace();
					 
				}
			}
		});
	}
	


	private void displaySelect(){
		String output = isTableSelected ? "select * from tableName": ("select * from "+GUITopLevel.selectedTable);
		inputArea.setText(output);
	}
	
	private void displayUpdate(){
		String output = isTableSelected ? "update (tableName) set        =        where condition" : "update " + GUITopLevel.selectedTable +  " set cell = new cell where conditions";
		inputArea.setText(output);
		
	}
	
	
	private void displayInsert(){
		String output = isTableSelected ? "insert into table name (column names) values ( )": "insert into "+(GUITopLevel.selectedTable)+ " (column names) values ( )";
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
            System.out.println("Query Time: " + totalTime);
            GUITopLevel.lblTimer.setText(totalTime);
            
            if ( display_rs == null )
            {
               System.out.println("Null ResultSet returned from query");
            }
            meta = display_rs.getMetaData();
            //setResultTable(meta);
            //The actual number of columns retrieved has to be checked

            rsColCount = meta.getColumnCount();
            lineOut = new StringBuffer(100);
            int[] columnWidths = new int[rsColCount];
            int[] columnScales = new int[rsColCount];
            int[] columnPrecisions = new int[rsColCount];
            int[] columnTypes = new int[rsColCount];
            String[] columnNames = new String[rsColCount];
            for ( i = 0; i < rsColCount; i++ )
            {
               columnNames[i] = meta.getColumnName(i + 1);
               if (columnNames[i].indexOf("-") > 0)
            	   columnNames[i] = columnNames[i].substring(columnNames[i].indexOf("-") + 1);
               columnWidths[i] = meta.getColumnDisplaySize(i + 1);
               columnTypes[i] = meta.getColumnType(i + 1);
               columnScales[i] = meta.getScale(i + 1);
               columnPrecisions[i] = meta.getPrecision(i + 1);
               if ( columnNames[i].length() > columnWidths[i] )
                  columnWidths[i] = columnNames[i].length(); 
               lineOut.append(padString(columnNames[i],columnWidths[i]) + " ");
               outputArea.setText(lineOut.toString());
            }
            //if ( tinySQLGlobals.DEBUG )
            
             
           displayResults(display_rs);
         }
		 else if (inputQuery.toUpperCase().startsWith("INSERT")| inputQuery.toUpperCase().startsWith("UPDATE")){
			 stmt.executeUpdate(inputQuery);
			 outputArea.setText("DONE\n"); 	 
		 }
		 
		
	 }
	
	   private static String padString(String inputString, int padLength)
	   {
	      String outputString;
	      String blanks = "                                        ";
	      if ( inputString == null )
	         outputString = blanks + blanks + blanks;
	      else
	         outputString = inputString;
	      if ( outputString.length() > padLength )
	         return outputString.substring(0,padLength);
	      else
	         outputString = outputString + blanks + blanks + blanks;
	         return outputString.substring(0,padLength);
	   }
	   
	   public int displayResults(ResultSet rs) throws java.sql.SQLException
	   {
	      if (rs == null)
	      {
	         outputArea.setText("ERROR in displayResult(): No data in ResultSet");
	         return 0;
	      }
	      java.sql.Date testDate;
	      int numCols = 0,nameLength;
	      ResultSetMetaData meta = rs.getMetaData();
	      int cols = meta.getColumnCount();
	      int[] width = new int[cols];
	      String dashes = "=============================================";
	/*
	 *    Display column headers
	 */
	      boolean first=true;
	      StringBuffer head = new StringBuffer();
	      StringBuffer line = new StringBuffer();
	/*
	 *    Fetch each row
	 */
	      while (rs.next()) 
	      {
	/*
	 *       Get the column, and see if it matches our expectations
	 */
	         String text = new String();
	         for (int ii=0; ii<cols; ii++)
	         {
	            String value = rs.getString(ii+1);
	            if (first)
	            {
	               width[ii] = meta.getColumnDisplaySize(ii+1);
	               if ( tinySQLGlobals.DEBUG &
	                    meta.getColumnType(ii+1) == Types.DATE )
	               {
	                  testDate = rs.getDate(ii+1);
	                  outputArea.append("Value " + value + ", Date "
	                  + testDate.toString());
	               }
	               nameLength = meta.getColumnName(ii+1).length();
	               if ( nameLength > width[ii] ) width[ii] = nameLength;
	               line.append(padString(dashes+dashes,width[ii]));
	               line.append(" ");
	            }
	            text += padString(value, width[ii]);
	            text += " ";   // the gap between the columns
	         }
	         try
	         {
	            if (first) 
	            {
	               if ( spoolFileWriter != null ) 
	               {
	                  spoolFileWriter.write(head.toString() + newLine);
	                  spoolFileWriter.write(head.toString() + newLine);
	               } else {
	                 outputArea.append(head.toString()+ "\n");
	                 outputArea.append(line.toString()+ "\n");
	               }
	               first = false;
	            }
	            if ( spoolFileWriter != null ) 
	               spoolFileWriter.write(text + newLine);
	            else
	            	outputArea.append(text + "\n");
	            numCols++;
	         } catch ( Exception writeEx ) {
	        	 outputArea.append("Exception writing to spool file " 
	            + writeEx.getMessage());
	         }
	      }
	      return numCols;
	   }
	   
	   
	   //test method================================================================================================
	  public void setResultTable(ResultSetMetaData tableMeta ) throws SQLException{
		  int trsColCount = meta.getColumnCount();
          StringBuffer tlineOut = new StringBuffer(100);
          int[] columnWidths = new int[trsColCount];
          int[] columnScales = new int[trsColCount];
          int[] columnPrecisions = new int[trsColCount];
          int[] columnTypes = new int[trsColCount];
          String[] columnNames = new String[trsColCount];
          for ( i = 0; i < rsColCount; i++ )
          {
             columnNames[i] = meta.getColumnName(i + 1);
             if (columnNames[i].indexOf("-") > 0)
          	   columnNames[i] = columnNames[i].substring(columnNames[i].indexOf("-") + 1);
             columnWidths[i] = meta.getColumnDisplaySize(i + 1);
             columnTypes[i] = meta.getColumnType(i + 1);
             columnScales[i] = meta.getScale(i + 1);
             columnPrecisions[i] = meta.getPrecision(i + 1);
             if ( columnNames[i].length() > columnWidths[i] )
                columnWidths[i] = columnNames[i].length(); 
             String [][] test = null;
             resultTable = new JTable(test, columnNames);
          }  
	  }
	   
	   private String checkForCache(String originalSql) throws SQLException {
		   // get the cache table name
		   String[] result = getCacheTableName(originalSql);
		   String cacheTableName = result[0];
		   String newSql = result[1];
		   
		   // check if the cache has been created by taking the where clause and the from tables
		   ResultSet tables = GUITopLevel.con.getMetaData().getTables(null,null,null,null);
		   while (tables.next()) {
			   if (tables.getString("TABLE_NAME").equals(cacheTableName)) {
				   System.out.println(newSql);
				   return newSql;
			   }
		   }
		   
		   // no table exists so we need to create one...
		   // lets take out the select stuff... 
		   String tmpSql = "SELECT * " + originalSql.substring(originalSql.indexOf("FROM")).trim();
		   
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
			   String columnName = tableName.substring(tableName.indexOf("->") + 2)  + "-" + allValuesMeta.getColumnName(i);
			   
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
		   GUITopLevel.con.createStatement().executeQuery(createTable);
		   
		   // then go through all the results and insert it into the table
		   insert += ") VALUES ('";
		   while (allValues.next()) {
			   for (int i = 1; i <= dataSize; i ++) {
				   insert += allValues.getString(i);
				   if (i < dataSize)
					   insert += "','";
			   }
			   insert += "'), (";
		   }
		   
		   insert = insert.substring(0, insert.length() - 3);
		   GUITopLevel.con.createStatement().executeQuery(insert);
		   
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
		   String cacheTableName = "_"; 
		   
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
				   System.out.println(sql);
				   newSQL = newSQL.replaceAll(key + "\\.", " " + table + "-");
				   System.out.println(newSQL);
			   }
			   
			   // and save the talbe to the string
			   cacheTableName += table;
		   }
		   
		   // compress the rest of the string and append it also
		   where = sql.toUpperCase().indexOf("WHERE");
		   if (where > 0)
			   cacheTableName += sql.substring(where + 5).replaceAll(" ", "");
		   
		   newSQL += " FROM " + cacheTableName;
		   
		   // return the name
		   String[] result = new String[2];
		   result[0] = cacheTableName;
		   result[1] = newSQL;
		   return result;
	   }
}
