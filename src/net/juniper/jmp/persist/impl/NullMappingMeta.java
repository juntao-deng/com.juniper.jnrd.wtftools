package net.juniper.jmp.persist.impl;

/**
 * A special Null Instance for MappingMeta stands for an error message 
 * @author juntaod
 *
 */
public final class NullMappingMeta extends MappingMeta<Object> {
	private static final long serialVersionUID = 1529021253823283840L;
	public static NullMappingMeta INSTANCE = new NullMappingMeta();
	private NullMappingMeta() {
		super(null, null);
	}

	@Override
	protected void resolve(String dataSource, Class<Object> entity) {
	}

}
