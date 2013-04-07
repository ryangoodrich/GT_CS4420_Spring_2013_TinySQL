package com.sqlmagic.tinysql;


import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class GUIStructure extends JPanel {

	//GUI components
	private static JLabel lblTableHere, colLabel;
	private static JTextArea textArea;
	private static JTable Ctable;
	private static JPanel topPanel;
	private	static JScrollPane scrollPane, scrollPane2;
	
	//class variables
	private static String tName;
	private static Connection connect;
	private static int colCounter= 1;
	private static Vector<String> datavals;
	private static String [][] dataValues;
  private static String columnNames[] = { "Column ID", "Name", "Type", "" };
	
	
	public static void main(String[] args) throws IOException,SQLException {
		
		try {
			GUIStructure panel = new GUIStructure();
			panel.setVisible(true);
		} catch (Exception e) {
			if(tinySQLGlobals.DEBUG)
			e.printStackTrace();
		}
		
	}

	//method to get table name from top level
	public static void setTName(String selectedT){
		tName = selectedT;
		try {
			UpdateStructure();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// method to get connect from top level
	public static void setConnect(Connection conn){
		connect = conn;
	}
	
	/**
	 * Create the panel.
	 */
	public GUIStructure() {
		
		//Display Table Name
		JLabel lblTable = new JLabel("TABLE:");
		lblTableHere = new JLabel("");
		
		//Text area from Create Statement
		JLabel lblCreateStatement = new JLabel("Create Statement");
		textArea = new JTextArea(10,10);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
	// Add the table to a scrolling pane
		scrollPane2 = new JScrollPane(textArea);
		
		//Set up table that displays Column Information
		
		JLabel lblColumns = new JLabel("Columns");
		colLabel = new JLabel("");
		
		String dataValues[][] ={};
		topPanel = new JPanel();
		Ctable = new JTable( dataValues, columnNames );
		Ctable.setEnabled(false);
		// Add the table to a scrolling pane
		scrollPane = new JScrollPane( Ctable );
		topPanel.add(scrollPane);
		
	
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(topPanel, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTable)
							.addGap(12)
							.addComponent(lblTableHere))
						.addComponent(lblCreateStatement)
						.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblColumns)
							.addGap(6)
							.addComponent(colLabel)))
					.addGap(16))
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(18)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(lblTable)
							.addComponent(lblTableHere))
						.addGap(44)
						.addComponent(lblCreateStatement)
						.addGap(6)
						.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(lblColumns)
							.addComponent(colLabel))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(topPanel, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(19, Short.MAX_VALUE))
			);
			setLayout(groupLayout);		
	}

	//Update information based on selected Table
	private static void UpdateStructure() throws IOException,SQLException{
		
		lblTableHere.setText(tName);
		
		DatabaseMetaData dbMeta;
		int colWidth,colScale,colPrecision,colType;
    String tableName=null,colTypeName;
		ResultSet display_rs;
		StringBuffer lineOut, CreateState;
		colCounter = 0;
		
		
    dbMeta = connect.getMetaData();
    tableName = tName;
    display_rs = dbMeta.getColumns(null,null,tableName,null);

    datavals = new Vector<String>();
    while (display_rs.next())
    {
       lineOut = new StringBuffer(100);
       lineOut.append(padString(display_rs.getString(4),32));
       colTypeName = display_rs.getString(6);
       colType = display_rs.getInt(5);
       colWidth = display_rs.getInt(7);
       colScale = display_rs.getInt(9);
       colPrecision = display_rs.getInt(10);
       if ( colTypeName.equals("CHAR") )
       {
          colTypeName = colTypeName + "(" 
          + Integer.toString(colWidth) + ")";
       } else if ( colTypeName.equals("FLOAT") ) {
          colTypeName += "("+ Integer.toString(colPrecision)
          + "," + Integer.toString(colScale) + ")";
       }  
       datavals.add(Integer.toString(colCounter));
       datavals.add(padString(display_rs.getString(4),32));
       datavals.add(colTypeName);
       datavals.add(Integer.toString(colType));
       lineOut.append(padString(colTypeName,20) + padString(colType,12));

       colCounter++;
       
    }
    //Parse vector holding information into correct format for JTable
    colLabel.setText("(" + String.valueOf(colCounter) + ")");
    int row = 0;
    int col = 0;
  	String [][] dataValues= new String [colCounter][4]; 
    ListIterator iter = datavals.listIterator();
    CreateState = new StringBuffer(100);
    while (iter.hasNext()) {
        dataValues[row][col] = (String)iter.next();
        if (col==1){
        	CreateState.append("\"" + (dataValues[row][col]).replaceAll("\\s","") + "\" ");
        }
        if (col==2){
        	CreateState.append(dataValues[row][col]);
        	 if (row ==0){
            	CreateState.append(" PRIMARY KEY " + "NOT NULL, ");
            }
        	 if (row == (colCounter-1))
        		 CreateState.append(" NOT NULL)");
        	 else
        		 CreateState.append(" NOT NULL, ");
        }
        col++;
        if (col == 4){
        	col = 0;
        	row++;
        }
    }
    //Display new column information
    Ctable.setModel(new DefaultTableModel(dataValues, columnNames));
    //Display new Create Statement information
    textArea.setText("CREATE TABLE \""+ tableName + "\" (" + CreateState);
	}
	

private static String padString(int inputint, int padLength)
{
   return padString(Integer.toString(inputint),padLength);
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
	
}
	


