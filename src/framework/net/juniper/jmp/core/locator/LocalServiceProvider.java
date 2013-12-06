package net.juniper.jmp.core.locator;


public class LocalServiceProvider implements ServiceProvider {
	@Override
	public Object getService(String clazz) {
		return SpringWebContextHelper.getSpringCtx().getBean(clazz);
	}

}
