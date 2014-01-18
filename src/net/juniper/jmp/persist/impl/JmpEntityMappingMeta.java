package net.juniper.jmp.persist.impl;

import net.juniper.jmp.entity.JmpEntity;

public class JmpEntityMappingMeta extends MappingMeta<JmpEntity> {
	private static final long serialVersionUID = 4174445832650908959L;
	public JmpEntityMappingMeta(String dataSource, Class<JmpEntity> clazz) {
		super(dataSource, clazz);
	}


	@Override
	protected void resolve(String dataSource, Class<JmpEntity> clazz) {

	}

}
