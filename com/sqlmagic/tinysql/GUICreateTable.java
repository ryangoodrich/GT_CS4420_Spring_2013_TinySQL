package com.sqlmagic.tinysql;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


public class GUICreateTable extends JFrame
{
	//GUI components
	private JTextArea tableArea, coltext, coltext_1, coltext_2, coltext_3, coltext_4,
	coltext_5,coltext_6,coltext_7,coltext_8,coltext_9;
	private JComboBox DTcomboBox, DTcomboBox_1,DTcomboBox_2,DTcomboBox_3,DTcomboBox_4,
	DTcomboBox_5,DTcomboBox_6,DTcomboBox_7,DTcomboBox_8,DTcomboBox_9;
	private JButton enterButton, cancelButton;
	private JLabel inputLabel, colLabel, colNameLabel, colTypeLabel;
	private JScrollPane scrollpane;
	private JPanel p1,p2,p3;

	//class variables
	private Statement stmt;
	
	
    public GUICreateTable ()
    {
    	setTitle("Create Table"); 

  		this.getContentPane().setLayout(new BorderLayout() );  

  		//Panel declarations
  		p1 = new JPanel();  
  		p2 = new JPanel(); 
  		p3 = new JPanel();
  		p1.setPreferredSize(new Dimension(400, 100));
  		p2.setPreferredSize(new Dimension(400, 450));
  		p3.setPreferredSize(new Dimension(400, 50));
  		p1.setLayout(null);
  		p2.setLayout(null);
  		scrollpane = new JScrollPane(p2);
  		scrollpane.setPreferredSize(new Dimension(400,200));
  		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
  		this.getContentPane().add(p1, BorderLayout.NORTH);  
  		this.getContentPane().add(scrollpane, BorderLayout.CENTER);  
  		this.getContentPane().add(p3, BorderLayout.SOUTH); 
  		
  		//Labels
  		inputLabel = new JLabel("Table Name: ");
  		inputLabel.setBounds(125, 15, 100, 30);
  		p1.add(inputLabel);
      tableArea = new JTextArea();
      tableArea.setBounds(211, 19, 125, 30);
      p1.add(tableArea);
      
      colLabel = new JLabel("Define Columns:");
      colLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
      colLabel.setBounds(25, 70, 150, 30);
  		p1.add(colLabel);
  		
  		enterButton = new JButton("Enter");
  		enterButton.setBounds(200,320,70,25);
  		p3.add(enterButton);
  		cancelButton = new JButton("Cancel");
  		cancelButton.setBounds(300,320,70,25);
  		p3.add(cancelButton);
  		
  		
  		
  		colNameLabel = new JLabel("Column Name");
  		colNameLabel.setBounds(100,10,100,30);
  		p2.add(colNameLabel);
  		
  		colTypeLabel = new JLabel("Data Type");
  		colTypeLabel.setBounds(300,10,100,30);
  		p2.add(colTypeLabel);
  		
  	//text area for column name
  		coltext = new JTextArea();
  		coltext.setBounds(100, 50, 150, 30);
  		p2.add(coltext);
  		coltext_1 = new JTextArea();
  		coltext_1.setBounds(100, 90, 150, 30);
  		p2.add(coltext_1);
  		coltext_2 = new JTextArea();
  		coltext_2.setBounds(100, 130, 150, 30);
  		p2.add(coltext_2);
  		coltext_3 = new JTextArea();
  		coltext_3.setBounds(100, 170, 150, 30);
  		p2.add(coltext_3);
  		coltext_4 = new JTextArea();
  		coltext_4.setBounds(100, 210, 150, 30);
  		p2.add(coltext_4);
  		coltext_5 = new JTextArea();
  		coltext_5.setBounds(100, 250, 150, 30);
  		p2.add(coltext_5);
  		coltext_6 = new JTextArea();
  		coltext_6.setBounds(100, 290, 150, 30);
  		p2.add(coltext_6);
  		coltext_7 = new JTextArea();
  		coltext_7.setBounds(100, 330, 150, 30);
  		p2.add(coltext_7);
  		coltext_8 = new JTextArea();
  		coltext_8.setBounds(100, 370, 150, 30);
  		p2.add(coltext_8);
  		coltext_9 = new JTextArea();
  		coltext_9.setBounds(100, 410, 150, 30);
  		p2.add(coltext_9);
  
  		
  		//select column type
  		DTcomboBox = new JComboBox();
  		DTcomboBox.addItem(" ");
  		DTcomboBox.addItem("INT");
  		DTcomboBox.addItem("FLOAT");
  		DTcomboBox.addItem("CHAR");
  		DTcomboBox.addItem("DATE");
  		DTcomboBox.setBounds(300,50,100,30);
  		p2.add(DTcomboBox);
  		
  		DTcomboBox_1 = new JComboBox();
  		DTcomboBox_1.addItem(" ");
  		DTcomboBox_1.addItem("INT");
  		DTcomboBox_1.addItem("FLOAT");
  		DTcomboBox_1.addItem("CHAR");
  		DTcomboBox_1.addItem("DATE");
  		DTcomboBox_1.setBounds(300,90,100,30);
  		p2.add(DTcomboBox_1);
  		
  		DTcomboBox_2 = new JComboBox();
  		DTcomboBox_2.addItem(" ");
  		DTcomboBox_2.addItem("INT");
  		DTcomboBox_2.addItem("FLOAT");
  		DTcomboBox_2.addItem("CHAR");
  		DTcomboBox_2.addItem("DATE");
  		DTcomboBox_2.setBounds(300,130,100,30);
  		p2.add(DTcomboBox_2);
  		
  		DTcomboBox_3 = new JComboBox();
  		DTcomboBox_3.addItem(" ");
  		DTcomboBox_3.addItem("INT");
  		DTcomboBox_3.addItem("FLOAT");
  		DTcomboBox_3.addItem("CHAR");
  		DTcomboBox_3.addItem("DATE");
  		DTcomboBox_3.setBounds(300,170,100,30);
  		p2.add(DTcomboBox_3);
  		
  		DTcomboBox_4 = new JComboBox();
  		DTcomboBox_4.addItem(" ");
  		DTcomboBox_4.addItem("INT");
  		DTcomboBox_4.addItem("FLOAT");
  		DTcomboBox_4.addItem("CHAR");
  		DTcomboBox_4.addItem("DATE");
  		DTcomboBox_4.setBounds(300,210,100,30);
  		p2.add(DTcomboBox_4);
  		
   		DTcomboBox_5 = new JComboBox();
  		DTcomboBox_5.addItem(" ");
  		DTcomboBox_5.addItem("INT");
  		DTcomboBox_5.addItem("FLOAT");
  		DTcomboBox_5.addItem("CHAR");
  		DTcomboBox_5.addItem("DATE");
  		DTcomboBox_5.setBounds(300,250,100,30);
  		p2.add(DTcomboBox_5);
  		
   		DTcomboBox_6 = new JComboBox();
  		DTcomboBox_6.addItem(" ");
  		DTcomboBox_6.addItem("INT");
  		DTcomboBox_6.addItem("FLOAT");
  		DTcomboBox_6.addItem("CHAR");
  		DTcomboBox_6.addItem("DATE");
  		DTcomboBox_6.setBounds(300,290,100,30);
  		p2.add(DTcomboBox_6);
  		
   		DTcomboBox_7 = new JComboBox();
  		DTcomboBox_7.addItem(" ");
  		DTcomboBox_7.addItem("INT");
  		DTcomboBox_7.addItem("FLOAT");
  		DTcomboBox_7.addItem("CHAR");
  		DTcomboBox_7.addItem("DATE");
  		DTcomboBox_7.setBounds(300,330,100,30);
  		p2.add(DTcomboBox_7);
  		
   		DTcomboBox_8 = new JComboBox();
  		DTcomboBox_8.addItem(" ");
  		DTcomboBox_8.addItem("INT");
  		DTcomboBox_8.addItem("FLOAT");
  		DTcomboBox_8.addItem("CHAR");
  		DTcomboBox_8.addItem("DATE");
  		DTcomboBox_8.setBounds(300,370,100,30);
  		p2.add(DTcomboBox_8);
  		
   		DTcomboBox_9 = new JComboBox();
  		DTcomboBox_9.addItem(" ");
  		DTcomboBox_9.addItem("INT");
  		DTcomboBox_9.addItem("FLOAT");
  		DTcomboBox_9.addItem("CHAR");
  		DTcomboBox_9.addItem("DATE");
  		DTcomboBox_9.setBounds(300,410,100,30);
  		p2.add(DTcomboBox_9);
  		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tableArea, scrollpane, coltext, coltext_1, coltext_2, coltext_3, coltext_4, coltext_5, coltext_6, coltext_7, coltext_8, coltext_9, DTcomboBox, DTcomboBox_1, DTcomboBox_2, DTcomboBox_3, DTcomboBox_4, DTcomboBox_5, DTcomboBox_6, DTcomboBox_7, DTcomboBox_8, DTcomboBox_9, enterButton, cancelButton}));
  	
  		
  		//Action events for buttons
  		
  		enterButton.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent arg0) {
  				StringBuffer lineOut;
  	  		lineOut = new StringBuffer(150);
  	  		//Construct Create Table string
  	  		lineOut.append("Create Table " + tableArea.getText() + " (");
  	  		if(coltext.getText().trim().isEmpty() == false)
  	  			lineOut.append("\"" + coltext.getText() + "\" "+ DTcomboBox.getSelectedItem());
  	  		if(coltext_1.getText().trim().isEmpty() == false)
  	  			lineOut.append(", \"" + coltext_1.getText() + "\" "+ DTcomboBox_1.getSelectedItem());
  	  		if(coltext_2.getText().trim().isEmpty() == false)
  	  			lineOut.append(", \"" + coltext_2.getText() + "\" "+ DTcomboBox_2.getSelectedItem());
  	  		if(coltext_3.getText().trim().isEmpty() == false)
  	  			lineOut.append(", \"" + coltext_3.getText() + "\" "+ DTcomboBox_3.getSelectedItem());
  	  		if(coltext_4.getText().trim().isEmpty() == false)
  	  			lineOut.append(", \"" + coltext_4.getText() + "\" "+ DTcomboBox_4.getSelectedItem());
  	  		if(coltext_5.getText().trim().isEmpty() == false)
    				lineOut.append(", \"" + coltext_5.getText() + "\" "+ DTcomboBox_5.getSelectedItem());
  	  		if(coltext_6.getText().trim().isEmpty() == false)
    				lineOut.append(", \"" + coltext_6.getText() + "\" "+ DTcomboBox_6.getSelectedItem());
  	  		if(coltext_7.getText().trim().isEmpty() == false)
    				lineOut.append(", \"" + coltext_7.getText() + "\" "+ DTcomboBox_7.getSelectedItem());
  	  		if(coltext_8.getText().trim().isEmpty() == false)
    				lineOut.append(", \"" + coltext_8.getText() + "\" "+ DTcomboBox_8.getSelectedItem());
  	  		if(coltext_9.getText().trim().isEmpty() == false)
    				lineOut.append(", \"" + coltext_9.getText() + "\" "+ DTcomboBox_9.getSelectedItem());
  				lineOut.append(")");
  				
  				try{
  					execute(lineOut.toString());
  					} catch (SQLException e) {
  					// TODO Auto-generated catch block
  					
  					JOptionPane.showMessageDialog(null, "Table Name Can't be Null");
  					if(tinySQLGlobals.DEBUG)
  						e.printStackTrace();
  					
  				}
  					dispose();
  			}
  		});
  		
  		cancelButton.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent arg0) {
  					dispose();	
  			}
  		});

  		
    }
    
    //Execute create table statement
    public void execute(String inputQuery) throws SQLException{

  		stmt = GUITopLevel.con.createStatement();
  		stmt.executeQuery(inputQuery);
  		
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
}