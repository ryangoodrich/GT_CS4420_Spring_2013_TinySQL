package com.sqlmagic.tinysql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JCheckBoxMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class GUITopLevel extends JFrame {

	// public variales Amelia and Herman will need
	public static Connection con = null;
	public static String selectedTable; 
	// at the bottom there are public methods to change the status or table list
	
	// panel variables
	GUIStructure struct;
	GUIExecuteSQL exec_panel;
	
	// class variables needed to perform operations
	private static Vector tableList;
	private static String fName;
	private boolean echo = false;
	private static DatabaseMetaData dbMeta;
	private static String dbType,dbVersion;
	
	// GUI components
	private JPanel contentPane;
	private static JButton btnBrowse;
	private static JLabel lblstatusHere;
	private static JLabel lblTablesInDirectory;
	private static JButton btnGo;
	private static JTextField textField;
	private static JList list;
	private static JCheckBoxMenuItem chckbxmntmSetDebugOnoff;
	private static JCheckBoxMenuItem chckbxmntmSetEchoOnoff;
	private static JCheckBoxMenuItem chckbxmntmSetParserDebug;
	private static JCheckBoxMenuItem chckbxmntmSetWhereDebug;
	private static JCheckBoxMenuItem chckbxmntmSetExDebug;
	private static JMenuItem mntmCreateTable;
	private JMenuItem mntmExit;
	private JMenuItem mntmCommands;
	private JMenuItem mntmLimitations;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmAbout;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws IOException,SQLException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		try {
			GUITopLevel frame = new GUITopLevel();
			frame.setVisible(true);
		} catch (Exception e) {
			if(tinySQLGlobals.DEBUG)
			e.printStackTrace();
		}
				
		// "real" main code starts here
						 
	      try 
	      {
	/*
	 *       Register the JDBC driver for dBase
	 */
	         Class.forName("com.sqlmagic.tinysql.dbfFileDriver");
	      } catch (ClassNotFoundException e) {
	         System.err.println(
	              "JDBC Driver could not be registered!!\n");
	         if ( tinySQLGlobals.DEBUG ) e.printStackTrace();
	      }
	      fName = ".";
	      if ( args.length > 0 ) fName = args[0];
	/* 
	 *    Establish a connection to dBase
	 */
	      con = dbConnect(fName);
	      if ( con == null )
	      {
	         fName = ".";
	         con = dbConnect(fName);
	      }
	      dbMeta = con.getMetaData();
	      dbType = dbMeta.getDatabaseProductName();		
	      dbVersion = dbMeta.getDatabaseProductVersion();
		
	}

	/**
	 * Create the frame.
	 */
	public GUITopLevel() {
		
		setTitle("TinySQL_GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmCreateTable = new JMenuItem("Create Table");
		mnFile.add(mntmCreateTable);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmCommands = new JMenuItem("Commands");
		mnHelp.add(mntmCommands);
		
		mntmLimitations = new JMenuItem("Limitations");
		mnHelp.add(mntmLimitations);
		
		mntmNewMenuItem = new JMenuItem("New Features");
		mnHelp.add(mntmNewMenuItem);
		
		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		JMenu mnDebug = new JMenu("Debug");
		menuBar.add(mnDebug);
		
		chckbxmntmSetDebugOnoff = new JCheckBoxMenuItem("Set Debug On/Off");
		mnDebug.add(chckbxmntmSetDebugOnoff);
		
		chckbxmntmSetEchoOnoff = new JCheckBoxMenuItem("Set Echo On/Off");
		mnDebug.add(chckbxmntmSetEchoOnoff);
		
		chckbxmntmSetParserDebug = new JCheckBoxMenuItem("Set Parser Debug On/Off");
		mnDebug.add(chckbxmntmSetParserDebug);
		
		chckbxmntmSetWhereDebug = new JCheckBoxMenuItem("Set Where Debug On/Off");
		mnDebug.add(chckbxmntmSetWhereDebug);
		
		chckbxmntmSetExDebug = new JCheckBoxMenuItem("Set Ex Debug On/Off");
		mnDebug.add(chckbxmntmSetExDebug);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		struct = new GUIStructure();
		JScrollPane jscroll1 = new JScrollPane();
		jscroll1.setViewportView(struct);
		
		tabbedPane.addTab("Table Structure", jscroll1);
		
		exec_panel = new GUIExecuteSQL();
		JScrollPane jscroll2 = new JScrollPane();
		jscroll2.setViewportView(exec_panel);
		
		tabbedPane.addTab("Execute  SQL", jscroll2);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 44, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(tabbedPane)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		lblTablesInDirectory = new JLabel(" ");
		lblTablesInDirectory.setHorizontalAlignment(SwingConstants.LEFT);
		lblTablesInDirectory.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(list, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
						.addComponent(lblTablesInDirectory, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblTablesInDirectory, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(list, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JLabel lblDirectory = new JLabel("Directory");
		lblDirectory.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		textField = new JTextField();
		textField.setColumns(10);
		
		btnBrowse = new JButton("Browse");
		btnBrowse.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblstatusHere = new JLabel("");
		
		btnGo = new JButton("Go");

		btnGo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDirectory)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnBrowse)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnGo)
					.addPreferredGap(ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
					.addComponent(lblStatus)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblstatusHere, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDirectory)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblstatusHere)
						.addComponent(lblStatus)
						.addComponent(btnBrowse)
						.addComponent(btnGo))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
		
		createEvents();
	}
	
	private void createEvents()  {
		
		// event handler for browse button click
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result;
				File dir;
				JFileChooser dialog = new JFileChooser();
				
				dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				result = dialog.showDialog(getParent(), "Select Directory");
				
				if(result == JFileChooser.APPROVE_OPTION){
					textField.setText(dialog.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		// event handler for go button click
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tinySQLDir = textField.getText();
			  
				try {
					con = dbConnect(tinySQLDir);
					dbMeta = con.getMetaData();
					dbType = dbMeta.getDatabaseProductName();		
					dbVersion = dbMeta.getDatabaseProductVersion();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					if(tinySQLGlobals.DEBUG)
					e.printStackTrace();
				}
			}
		});
		
		// so that we can figure out the selected table in the list
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int idx = list.getSelectedIndex();
				if(!e.getValueIsAdjusting() && idx != -1){
					selectedTable = (String)(list.getSelectedValue());
				}
			}
		});
		
		// debug check box menu item
		chckbxmntmSetDebugOnoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxmntmSetDebugOnoff.isSelected()){
					tinySQLGlobals.DEBUG = true;
				}
				else
					tinySQLGlobals.debug = false;
			}
		});
		
		//  set Echo on/off
		chckbxmntmSetEchoOnoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxmntmSetEchoOnoff.isSelected())
					echo = true;
				else
					echo = false;
			}
		});
		
		// set parser debug on/off
		chckbxmntmSetParserDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxmntmSetParserDebug.isSelected())
					tinySQLGlobals.PARSER_DEBUG = true;
				else
					tinySQLGlobals.PARSER_DEBUG = false;
			}
		});
		
		// set where debug on/off
		chckbxmntmSetWhereDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxmntmSetWhereDebug.isSelected())
					tinySQLGlobals.WHERE_DEBUG = true;
				else
					tinySQLGlobals.WHERE_DEBUG = false;
			}
		});
		
		// file buttons
		
		// set Ex debug on/off
		chckbxmntmSetExDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxmntmSetExDebug.isSelected())
					tinySQLGlobals.EX_DEBUG = true;
				else 
					tinySQLGlobals.EX_DEBUG = false;
			}
		});
		
		// menu button clicked to open create table form
		mntmCreateTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// will be some code here to open the create table form
			}
		});
		
		// exit button in file
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		// help menu handlers
		
		// equivalent to "help commands" function 
		mntmCommands.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getParent(),
					    helpMsg("help commands"),"Normally Available Commands",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		// equivalent to "help limitations" function
		mntmLimitations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getParent(),
					    helpMsg("help limitations"),"Limitations of TinySQL",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		//  equivalent to "Help new" command
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getParent(),
					    helpMsg("help new"),"New features of TinySQL",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		// equivalent to "help about" command
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(getParent(),
					    helpMsg("help about"),"About TinySQL",JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	   private static Connection dbConnect(String tinySQLDir) throws SQLException
	   {
	      Connection conn=null;
	      DatabaseMetaData dbMeta;
	      File conPath;
	      File[] fileList;
	      String tableName;
	      ResultSet tables_rs;
	      conPath = new File(tinySQLDir);
	      fileList = conPath.listFiles();
	      if ( fileList == null )
	      {
	    	 lblstatusHere.setText(tinySQLDir + " is not a valid directory.");
	    	 lblstatusHere.setForeground(Color.RED);
	         return null;
	      } else {
	    	 lblstatusHere.setText("Connecting to " + conPath.getAbsolutePath());
	    	 lblstatusHere.setForeground(Color.GREEN);
	         conn = DriverManager.getConnection("jdbc:dbfFile:" + conPath, "", "");
	      }
	      dbMeta = conn.getMetaData();
	      tables_rs = dbMeta.getTables(null,null,null,null);
	      tableList = new Vector();
	      while ( tables_rs.next() )
	      {
	         tableName = tables_rs.getString("TABLE_NAME");
	         tableList.addElement(tableName);
	      }
	      if ( tableList.size() == 0 ){
	    	  lblTablesInDirectory.setText("No tables in this directory.");
	      	  lblTablesInDirectory.setForeground(Color.RED);
	      }
	      else {
	    	  lblTablesInDirectory.setText(tableList.size() + " tables"
	         + " in this directory.");
	    	  lblTablesInDirectory.setForeground(Color.GREEN);
	    	  
	    	// call a method that takes the tablelist and puts them in the JList
	    	list.setListData(tableList);
	    	list.setSelectedIndex(0);
	    	  
	      }
	      return conn;
	   }
	   
	   private static String helpMsg(String inputCmd)
	   {
	      String upperCmd;
	      upperCmd = inputCmd.toUpperCase().trim();
	      if ( upperCmd.equals("HELP") )
	      {
	         return("The following help topics are available:\n"
	         + "=============================================================\n"
	         + "HELP NEW - list of new features in tinySQL " + dbVersion + "\n"
	         + "HELP COMMANDS - help for the non-SQL commands\n"
	         + "HELP LIMITATIONS - limitations of tinySQL " + dbVersion + "\n"
	         + "HELP ABOUT - short description of tinySQL.\n");
	      } else if ( upperCmd.equals("HELP COMMANDS") ) {
	         return("The following non-SQL commands are supported:\n"
	         + "=============================================================\n"
	         + "SHOW TABLES - lists the tinySQL tables (DBF files) in the current "
	         + "directory\n"
	         + "SHOW TYPES - lists column types supported by tinySQL.\n"
	         + "DESCRIBE table_name - describes the columns in table table_name.\n"
	         + "CONNECT directory - connects to a different directory;\n"
	         + "   Examples:  CONNECT C:\\TEMP in Windows\n"
	         + "              CONNECT /home/mydir/temp in Linux/Unix\n"
	         + "SET DEBUG ON/OFF - turns general debugging on or off\n"
	         + "SET PARSER_DEBUG ON/OFF - turns parser debugging on or off\n"
	         + "SET WHERE_DEBUG ON/OFF - turns where clause debugging on or off\n"
	         + "SET EX_DEBUG ON/OFF - turns exception stack printing on or off\n"
	         + "SET ECHO ON/OFF - will echo input commands\n"
	         + "EXIT - leave the tinySQL command line interface.\n");
	      } else if ( upperCmd.equals("HELP LIMITATIONS") ) {
	         return("tinySQL " + dbVersion 
	         + " does NOT support the following:\n"
	         + "=============================================================\n"
	         + "Subqueries: eg SELECT COL1 from TABLE1 where COL2 in (SELECT ...\n"
	         + "IN specification within a WHERE clause.\n"
	         + "GROUP BY clause in SELECT statments.\n"
	         + "AS in CREATE statements; eg CREATE TABLE TAB2 AS SELECT ...\n"
	         + "UPDATE statements including JOINS.\n\n"
	         + "If you run into others let us know by visiting\n"
	         + "http://sourceforge.net/projects/tinysql\n");
	      } else if ( upperCmd.equals("HELP NEW") ) {
	         return("New features in tinySQL releases.\n"
	         + "=============================================================\n"
	         + "Version 2.26d released December 29, 2006\n"
	         + "Corrected problems with date comparisions, added support for \n"
	         + "the TO_DATE function, corrected problems with DELETE.\n"
	         + "Added support for IS NULL and IS NOT NULL, added ability\n"
	         + "to spool output to a file.\n"
	         + "---------------------------------------------------------------\n"
	         + "Version 2.10 released October 22, 2006\n"
	         + "Added support for long column names and fixed bugs in \n"
	         + "ALTER TABLE commands.\n"
	         + "---------------------------------------------------------------\n"
	         + "Version 2.02 released July 20, 2005\n"
	         + "Fixed more bugs with the COUNT(*) and the like comparison.\n"
	         + "---------------------------------------------------------------\n"
	         + "Version 2.01 released April 20, 2005\n"
	         + "Fixed several bugs with the COUNT(*) and other summary functions\n"
	         + "Fixed ORDER BY using columns that were not SELECTed.\n"
	         + "Added support for DISTINCT keyword and TRIM function.\n"
	         + "Added default sorting by selected column.\n"
	         + "Significant reorganization of code to allow the use of functions\n"
	         + "in WHERE clauses (now stores tsColumn objects in tinySQLWhere).\n"
	         + "---------------------------------------------------------------\n"
	         + "Version 2.0 released Dec. 20, 2004\n" 
	         + "The package name was changed to com.sqlmagic.tinysql.\n"
	         + "Support for table aliases in JOINS: see example below\n"
	         + "  SELECT A.COL1,B.COL2 FROM TABLE1 A,TABLE2 B WHERE A.COL3=B.COL3\n"
	         + "COUNT,MAX,MIN,SUM aggregate functions.\n"
	         + "CONCAT,UPPER,SUBSTR in-line functions for strings.\n"
	         + "SYSDATE - current date.\n"
	         + "START script_file.sql - executes SQL commands in file.\n"
	         + "Support for selection of constants: see example below:\n"
	         + "  SELECT 'Full Name: ',first_name,last_name from person\n"
	         + "All comparisions work properly: < > = != LIKE \n");
	      } else if ( upperCmd.equals("HELP ABOUT") ) {
	         return(
	           "=============================================================\n"
	         + "tinySQL was originally written by Brian Jepson\n"
	         + "as part of the research he did while writing the book \n"
	         + "Java Database Programming (John Wiley, 1996).  The database was\n"
	         + "enhanced by Andreas Kraft, Thomas Morgner, Edson Alves Pereira,\n"
	         + "and Marcel Ruff between 1997 and 2002.\n"
	         + "The current version " + dbVersion
	         + " was developed by Davis Swan starting in 2004.\n\n"
	         + "tinySQL is free software; you can redistribute it and/or\n"
	         + "modify it under the terms of the GNU Lesser General Public\n"
	         + "License as published by the Free Software Foundation; either\n"
	         + "version 2.1 of the License, or (at your option) any later version.\n"
	         + "This library is distributed in the hope that it will be useful,\n"
	         + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
	         + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU\n"
	         + "Lesser General Public License for more details at\n"
	         + "http://www.gnu.org/licenses/lgpl.html");
	      } else {
	         return("Unknown help command.\n");
	      }
	   }
	   
	   public static void UpdateStatusLabel(String message){
		   lblstatusHere.setText(message);
		   lblstatusHere.setForeground(Color.black);
	   }
	   
	   public static void UpdateStatusLabel(String message, Color color){
		   lblstatusHere.setText(message);
		   lblstatusHere.setForeground(color);
	   }
	   
	   public static void setTableList(Object[] l){
		   list.setListData(l);
		   list.setSelectedIndex(0);
	   }
	   
	   public static void setTableList(Object[] l, int index){
		   list.setListData(l);
		   list.setSelectedIndex(index);
	   }
	   
	   public static void setTableList(Vector l){
		   list.setListData(l);
		   list.setSelectedIndex(0);
	   }
	   
	   public static void setTableList(Vector[] l, int index){
		   list.setListData(l);
		   list.setSelectedIndex(index);
	   }
}
