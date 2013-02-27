/*
 *
 * Connection class for the textFile/tinySQL
 * JDBC driver
 *
 * A lot of this code is based on or directly taken from
 * George Reese's (borg@imaginary.com) mSQL driver.
 *
 * So, it's probably safe to say:
 *
 * Portions of this code Copyright (c) 1996 George Reese
 *
 * The rest of it:
 * Copyright 1996, Brian C. Jepson
 *                 (bjepson@ids.net)
 *
 * $Author: davis $
 * $Date: 2004/12/18 21:29:35 $
 * $Revision: 1.1 $
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package com.sqlmagic.tinysql;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class textFileConnection extends tinySQLConnection {

  /**
   *
   * Constructs a new JDBC Connection object.
   *
   * @exception SQLException in case of an error
   * @param user the user name - not currently used
   * @param u the url to the data source
   * @param d the Driver object
   *
   */
  public textFileConnection(String user, String u, Driver d) 
         throws SQLException {
    super(user, u, d);
  }

  /**
   *
   * Returns a new textFile object which is cast to a tinySQL
   * object.
   *
   */
  @Override
public tinySQL get_tinySQL() {
     return new textFile();
  }

@Override
public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Blob createBlob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Clob createClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public NClob createNClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public SQLXML createSQLXML() throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Statement createStatement(int arg0, int arg1, int arg2)
		throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Properties getClientInfo() throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getClientInfo(String arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public int getHoldability() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public boolean isValid(int arg0) throws SQLException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3)
		throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public PreparedStatement prepareStatement(String arg0, int arg1)
		throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public PreparedStatement prepareStatement(String arg0, int[] arg1)
		throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public PreparedStatement prepareStatement(String arg0, String[] arg1)
		throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
		int arg3) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void releaseSavepoint(Savepoint arg0) throws SQLException {
	// TODO Auto-generated method stub
	
}

@Override
public void rollback(Savepoint arg0) throws SQLException {
	// TODO Auto-generated method stub
	
}

@Override
public void setClientInfo(Properties arg0) throws SQLClientInfoException {
	// TODO Auto-generated method stub
	
}

@Override
public void setClientInfo(String arg0, String arg1)
		throws SQLClientInfoException {
	// TODO Auto-generated method stub
	
}

@Override
public void setHoldability(int arg0) throws SQLException {
	// TODO Auto-generated method stub
	
}

@Override
public Savepoint setSavepoint() throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Savepoint setSavepoint(String arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setTypeMap(Map map) throws SQLException {
	// TODO Auto-generated method stub
	
}

@Override
public boolean isWrapperFor(Class<?> arg0) throws SQLException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public <T> T unwrap(Class<T> arg0) throws SQLException {
	// TODO Auto-generated method stub
	return null;
}

}
