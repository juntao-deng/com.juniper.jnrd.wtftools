package net.juniper.jmp.persist;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import net.juniper.jmp.persist.jdbc.ParamTypeFactory;

public class SQLParameter implements Serializable {
	private static final long serialVersionUID = 3057332678654807151L;
	private List<Object> paramList = new ArrayList<Object>();

	public SQLParameter() {
	}

	public void addNullParam(int type) {
		paramList.add(ParamTypeFactory.getNullType(type));
	}

	public void addBlobParam(Object blob) {
		if (blob == null)
			addNullParam(Types.BLOB);
		else
			paramList.add(ParamTypeFactory.getBlobType(blob));
	}

	public void addBlobParam(byte[] bytes) {
		if (bytes == null)
			addNullParam(Types.BLOB);
		else
			paramList.add(ParamTypeFactory.getBlobType(bytes));
	}

	public void addBlobParam(InputStream stream) {
		if (stream == null)
			addNullParam(Types.BLOB);
		else
			paramList.add(ParamTypeFactory.getBlobType(stream));
	}

	public void addClobParam(String clobStr) {
		if (clobStr == null)
			addNullParam(Types.CLOB);
		else
			paramList.add(ParamTypeFactory.getClobType(clobStr));
	}


	public void addParam(Object param) {
		paramList.add(param);

	}

	public void addParam(Integer param) {
		if (param == null) {
			addNullParam(Types.INTEGER);
		} 
		else {
			paramList.add(param);
		}
	}

	public void addParam(String param) {
		if (param == null) {
			addNullParam(Types.VARCHAR);
		}
		else {
			paramList.add(param);
		}
	}

	public void addParam(int param) {
		paramList.add(new Integer(param));
	}

	public void addParam(long param) {
		paramList.add(new Long(param));
	}

	public void addParam(double param) {
		paramList.add(new Double(param));
	}

	public void addParam(boolean param) {
		paramList.add(Boolean.valueOf(param));
	}
	
	public void addParam(float param) {
		paramList.add(new Float(param));
	}

	public void addParam(short param) {
		paramList.add(new Short(param));
	}

	public Object get(int index) {
		return paramList.get(index);
	}

	public void replace(int index, Object param) {
		paramList.set(index, param);
	}

	public void replace(int index, String param) {
		paramList.remove(index);
		if (param == null)
			paramList.add(ParamTypeFactory.getNullType(Types.VARCHAR));
		else
			paramList.add(index, param);
	}


	public void clear() {
		paramList.clear();
	}

	public int getCount() {
		return paramList.size();
	}

	public List<Object> getParameters() {
		return paramList;
	}
}
