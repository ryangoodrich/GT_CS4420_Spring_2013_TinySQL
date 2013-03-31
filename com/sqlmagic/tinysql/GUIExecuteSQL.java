package com.sqlmagic.tinysql;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class GUIExecuteSQL extends JPanel {

	
	
	JTextArea inputArea, outputArea;
	JButton selectButton, executeButton, insertButton, updateButton, createButton, deleteButton;
	JLabel inputLabel, outputLabel;
	JScrollPane scrollPane;
	ResultSet display_rs;
	Statement stmt;
	ResultSetMetaData meta;
	StringBuffer lineOut;
	int rsColCount, i;
	FileWriter spoolFileWriter = null;
	String newLine = System.getProperty("line.separator"); 
	
	
	public GUIExecuteSQL() {
		//set layout
		this.setLayout(null);
		
		//Labels
		inputLabel = new JLabel("Enter SQL");
		inputLabel.setBounds(15, 10, 50, 30);
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
	    scrollPane = new JScrollPane(outputArea);
		scrollPane.setBounds(15, 150, 630, 250);
		this.add(scrollPane);
		
		//Buttons
		selectButton = new JButton("Select");
		selectButton.setBounds(250,10,70,25);
		this.add(selectButton);
		insertButton = new JButton("Insert");
		insertButton.setBounds(320,10,70,25);
		this.add(insertButton);
		updateButton = new JButton("Update");
		updateButton.setBounds(390,10,70,25);
		this.add(updateButton);
		executeButton = new JButton("Run SQL");
		executeButton.setBounds(450,100,100,30);
		this.add(executeButton);
		
		
		
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				display();
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
	


	private void display(){
		inputArea.setText("select * from tableName");
	}
	
	
	public void execute(String inputQuery) throws SQLException{

		stmt = GUITopLevel.con.createStatement();
		
		 //if ( inputQuery.toUpperCase().startsWith("SELECT") ) 
         //{
            display_rs = stmt.executeQuery(inputQuery);
            if ( display_rs == null )
            {
               System.out.println("Null ResultSet returned from query");
            }
            meta = display_rs.getMetaData();

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
        // }
		
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
	               head.append(padString(meta.getColumnName(ii+1), width[ii]));
	               head.append(" ");
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
	   

}
