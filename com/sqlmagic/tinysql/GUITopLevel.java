package com.sqlmagic.tinysql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
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

	// GUI components
	private JPanel contentPane;
	private static JButton btnBrowse;
	private static JLabel lblstatusHere;
	private static JLabel lblTablesInDirectory;
	private JButton btnGo;
	private JTextField textField;
	private static JList list;
	
	// class variables needed to perform operations
	private static Vector tableList;
	private static Connection con = null;
	private static String fName;
	private boolean echo;
	private static DatabaseMetaData dbMeta;
	private static String dbType,dbVersion;
	private static String selectedTable; 
	
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
		
		JMenuItem mntmCreateTable = new JMenuItem("Create Table");
		mnFile.add(mntmCreateTable);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmCommands = new JMenuItem("Commands");
		mnHelp.add(mntmCommands);
		
		JMenuItem mntmLimitations = new JMenuItem("Limitations");
		mnHelp.add(mntmLimitations);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New Features");
		mnHelp.add(mntmNewMenuItem);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		JMenu mnDebug = new JMenu("Debug");
		menuBar.add(mnDebug);
		
		JCheckBoxMenuItem chckbxmntmSetDebugOnoff = new JCheckBoxMenuItem("Set Debug On/Off");
		mnDebug.add(chckbxmntmSetDebugOnoff);
		
		JCheckBoxMenuItem chckbxmntmSetEchoOnoff = new JCheckBoxMenuItem("Set Echo On/Off");
		mnDebug.add(chckbxmntmSetEchoOnoff);
		
		JCheckBoxMenuItem chckbxmntmSetParserDebug = new JCheckBoxMenuItem("Set Parser Debug On/Off");
		mnDebug.add(chckbxmntmSetParserDebug);
		
		JCheckBoxMenuItem chckbxmntmSetWhereDebug = new JCheckBoxMenuItem("Set Where Debug On/Off");
		mnDebug.add(chckbxmntmSetWhereDebug);
		
		JCheckBoxMenuItem chckbxmntmSetExDebug = new JCheckBoxMenuItem("Set Ex Debug On/Off");
		mnDebug.add(chckbxmntmSetExDebug);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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
	    	 lblstatusHere.setText(tinySQLDir + " is not a valid directory.");
	    	 lblstatusHere.setForeground(Color.RED);
	         return null;
	      } else {
	    	 lblstatusHere.setText("Connecting to " + conPath.getAbsolutePath());
	    	 lblstatusHere.setForeground(Color.GREEN);
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
	      return con;
	   }
}
