package net.juniper.jmp.persist.impl;
/**
 * Mapping meta from JPA Entity to Db table
 * @author juntaod
 *
 */
public final class JpaEntityMappingMeta extends MappingMeta<Object> {
	private static final long serialVersionUID = 8015429393959008969L;
	public JpaEntityMappingMeta(Class<Object> clazz) {
		super(clazz);
	}

	@Override
	public void resolve(Class<Object> entity) {
		
	}
}
