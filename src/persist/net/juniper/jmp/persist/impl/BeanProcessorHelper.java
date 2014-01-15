package net.juniper.jmp.persist.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.juniper.jmp.persist.utils.SqlLogger;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.logging.Logger;

public abstract class BeanProcessorHelper {
	private Logger logger = Logger.getLogger(BeanProcessorHelper.class);
	protected Object getColumnValue(int type, ResultSet resultSet, int index) {
		Object value = null;
		try{
			switch (type) {
				case Types.BLOB:
				case Types.LONGVARBINARY:
				case Types.VARBINARY:
				case Types.BINARY:
					value = deserialize(resultSet.getBytes(index));
					break;
				case Types.CLOB:
					value = getClob(resultSet, index);
					break;
				default:
					value = resultSet.getObject(index);
					break;
			}
		}
		catch(SQLException e){
			SqlLogger.error(e.getMessage(), e);
		}
		return value;
	}
	
	private String getClob(ResultSet rs, int index) throws SQLException {
		Reader rsInput = rs.getCharacterStream(index);
		if (rsInput == null)
			return null;
		BufferedReader reader = new BufferedReader(rsInput);
		StringBuffer buffer = new StringBuffer();
		try {
			String c = reader.readLine();
			while (c != null) {
				buffer.append(c);
				c = reader.readLine();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			IOUtils.closeQuietly(reader);
		}
		return buffer.toString();
	}
	
	private Serializable deserialize(byte[] ba) {
		if (ba == null)
			return null;
		Serializable value = null;
		ObjectInputStream oinput = null;
		try {
			ByteArrayInputStream binput = new ByteArrayInputStream(ba);
			oinput = new ObjectInputStream(binput);
			value = (Serializable) oinput.readObject();
		} 
		catch (IOException e) {
			return ba;
		} 
		catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		finally{
			IOUtils.closeQuietly(oinput);
		}
		return value;
	}
	
}
