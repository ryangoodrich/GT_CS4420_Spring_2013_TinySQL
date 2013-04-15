package com.sqlmagic.tinysql;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;


public class GUICreateTable extends JFrame
{
	//GUI components
	private JTextArea tableArea, coltext, coltext_1, coltext_2, coltext_3, coltext_4;
	private JComboBox DTcomboBox, DTcomboBox_1,DTcomboBox_2,DTcomboBox_3,DTcomboBox_4;
	private JButton enterButton, cancelButton;
	private JLabel inputLabel, colLabel, colNameLabel, colTypeLabel;
	private JScrollPane scrollPane;
	private JPanel panel;
	
	//class variables
	private Statement stmt;
	
	//private static Vector<String> createStatement;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUICreateTable frame = new GUICreateTable();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    public GUICreateTable ()
    {
  		this.setLayout(null);
  		
  		//Labels
  		inputLabel = new JLabel("Table Name: ");
  		inputLabel.setBounds(150, 10, 100, 30);
  		this.add(inputLabel);
      tableArea = new JTextArea();
      tableArea.setBounds(250, 10, 150, 25);
      this.add(tableArea);
  	
      colLabel = new JLabel("Define Columns:");
      colLabel.setBounds(25, 70, 150, 30);
  		this.add(colLabel);
  		
  		//Buttons
   		enterButton = new JButton("Enter");
  		enterButton.setBounds(200,320,70,25);
  		this.add(enterButton);
  		cancelButton = new JButton("Cancel");
  		cancelButton.setBounds(300,320,70,25);
  		this.add(cancelButton);
      
  		panel = new JPanel();
  		panel.setBounds(30, 110, 550, 200);
  		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
  		this.add(panel);
  		
  		//Set layout of panel where user enters column info
  		panel.setLayout(null);
  		
  		
  		colNameLabel = new JLabel("Column Name");
  		colNameLabel.setBounds(100,10,100,30);
  		panel.add(colNameLabel);
  		
  		colTypeLabel = new JLabel("Data Type");
  		colTypeLabel.setBounds(300,10,100,30);
  		panel.add(colTypeLabel);
  		
  		//text area for column name
  		coltext = new JTextArea();
  		coltext.setBounds(100, 50, 150, 30);
  		panel.add(coltext);
  		coltext_1 = new JTextArea();
  		coltext_1.setBounds(100, 90, 150, 30);
  		panel.add(coltext_1);
  		coltext_2 = new JTextArea();
  		coltext_2.setBounds(100, 130, 150, 30);
  		panel.add(coltext_2);
  		coltext_3 = new JTextArea();
  		coltext_3.setBounds(100, 170, 150, 30);
  		panel.add(coltext_3);
  		coltext_4 = new JTextArea();
  		coltext_4.setBounds(100, 210, 150, 30);
  		panel.add(coltext_4);
  		
  		//select column type
  		DTcomboBox = new JComboBox();
  		DTcomboBox.addItem(" ");
  		DTcomboBox.addItem("INT");
  		DTcomboBox.addItem("FLOAT");
  		DTcomboBox.addItem("CHAR");
  		DTcomboBox.addItem("DATE");
  		DTcomboBox.setBounds(300,50,100,30);
  		panel.add(DTcomboBox);
  		
  		DTcomboBox_1 = new JComboBox();
  		DTcomboBox_1.addItem(" ");
  		DTcomboBox_1.addItem("INT");
  		DTcomboBox_1.addItem("FLOAT");
  		DTcomboBox_1.addItem("CHAR");
  		DTcomboBox_1.addItem("DATE");
  		DTcomboBox_1.setBounds(300,90,100,30);
  		panel.add(DTcomboBox_1);
  		
  		DTcomboBox_2 = new JComboBox();
  		DTcomboBox_2.addItem(" ");
  		DTcomboBox_2.addItem("INT");
  		DTcomboBox_2.addItem("FLOAT");
  		DTcomboBox_2.addItem("CHAR");
  		DTcomboBox_2.addItem("DATE");
  		DTcomboBox_2.setBounds(300,130,100,30);
  		panel.add(DTcomboBox_2);
  		
  		DTcomboBox_3 = new JComboBox();
  		DTcomboBox_3.addItem(" ");
  		DTcomboBox_3.addItem("INT");
  		DTcomboBox_3.addItem("FLOAT");
  		DTcomboBox_3.addItem("CHAR");
  		DTcomboBox_3.addItem("DATE");
  		DTcomboBox_3.setBounds(300,170,100,30);
  		panel.add(DTcomboBox_3);
  		
  		DTcomboBox_4 = new JComboBox();
  		DTcomboBox_4.addItem(" ");
  		DTcomboBox_4.addItem("INT");
  		DTcomboBox_4.addItem("FLOAT");
  		DTcomboBox_4.addItem("CHAR");
  		DTcomboBox_4.addItem("DATE");
  		DTcomboBox_4.setBounds(300,210,100,30);
  		panel.add(DTcomboBox_4);
 
  
  		//Action events for buttons
  		
  		enterButton.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent arg0) {
  				StringBuffer lineOut;
  	  		lineOut = new StringBuffer(100);
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
  				lineOut.append(")");
  				
  				try{
  					execute(lineOut.toString());
  					} catch (SQLException e) {
  					// TODO Auto-generated catch block
  					
  					JOptionPane.showMessageDialog(null, "Table Name Can't be Null");
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
  		
  		
  	 }
}