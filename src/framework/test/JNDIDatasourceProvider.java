package test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.jboss.resteasy.logging.Logger;

public class JNDIDatasourceProvider implements DataSourceProvider {
	private Logger logger = Logger.getLogger(JNDIDatasourceProvider.class);
	private Context context;
	@Override
	public DataSource getDataSource(String name) {
		try{
			return (DataSource) getContext().lookup(name);
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new JmpDbRuntimeException("error getting ds:" + name);
		}
	}

	@Override
	public XADataSource getXaDataSource(String name) {
		try{
			return (XADataSource) getContext().lookup(name);
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new JmpDbRuntimeException("error getting xa ds:" + name);
		}
	}

	private Context getContext() {
		try{
			if(context == null)
				context = new InitialContext();
			return context;
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new JmpDbRuntimeException("error getting context");
		}
	}
}
