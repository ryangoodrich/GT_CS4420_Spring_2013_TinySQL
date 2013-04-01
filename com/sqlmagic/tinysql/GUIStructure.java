package com.sqlmagic.tinysql;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;

public class GUIStructure extends JPanel {

	
	private static JLabel lblTableHere;
	private static String tName;
	private static JTextArea textArea;
	private static Connection connect;
	static Vector tableList;
	static FileWriter spoolFileWriter = null;
	static String newLine = System.getProperty("line.separator"); 
	//private static DatabaseMetaData dbMeta;
	//static GUITopLevel tl = new GUITopLevel();
	private JMenuItem mntmLimitations;
	
	public static void setTName(String selectedT){
		tName = selectedT;
	}
	
	public static void setConnect(Connection conn){
		connect = conn;
	}
	
	
	
	/**
	 * Create the panel.
	 */
	public GUIStructure() {
		
		JLabel lblCreateStatement = new JLabel("Create Statement");
		
		JLabel lblTable = new JLabel("TABLE:");
		
		lblTableHere = new JLabel("");
		
		JLabel lblColumns = new JLabel("Columns");
		
		JLabel label = new JLabel("( )");
		
		textArea = new JTextArea();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblColumns)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(label))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTable)
							.addGap(12)
							.addComponent(lblTableHere))
						.addComponent(lblCreateStatement))
					.addGap(43))
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
					.addGap(94)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblColumns)
						.addComponent(label))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(17, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		//textArea.setText("something");
		
		
	}
	
	private static Connection dbConnect(String tinySQLDir) throws SQLException
  {
     Connection con=null;
     DatabaseMetaData dbMeta;
     File conPath;
     File[] fileList;
     String tableName;
     ResultSet tables_rs;
     conPath = new File(tinySQLDir);
     fileList = conPath.listFiles();
     if ( fileList == null )
     {
        System.out.println(tinySQLDir + " is not a valid directory.");
        return null;
     } else {
        System.out.println("Connecting to " + conPath.getAbsolutePath());
        con = DriverManager.getConnection("jdbc:dbfFile:" + conPath, "", "");
     }
     dbMeta = con.getMetaData();
     tables_rs = dbMeta.getTables(null,null,null,null);
     tableList = new Vector();
     while ( tables_rs.next() )
     {
        tableName = tables_rs.getString("TABLE_NAME");
        tableList.addElement(tableName);
     }
     if ( tableList.size() == 0 )
        System.out.println("There are no tinySQL tables in this directory.");
     else
        System.out.println("There are " + tableList.size() + " tinySQL tables"
        + " in this directory.");
     return con;
  }

	
 
	
	
}
	


