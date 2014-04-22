package br.com.caelum.vraptor.routes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Property;
import br.com.caelum.vraptor.http.route.PathAnnotationRoutesParser;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Specializes
public class CustomRouterParser extends PathAnnotationRoutesParser {
	
	private Properties properties = new Properties();
	
	@Property(defaultValue="/routes.properties")
	private String routesFileName;
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected CustomRouterParser() {
		this(null, null);
	}
	
	@Inject
	public CustomRouterParser(Router router, String routesFileName) {
		super(router);
		this.routesFileName = routesFileName;
	}
	
	@PostConstruct
	public void postConstruct() {
		try {
			properties.load(getClass().getResourceAsStream(routesFileName));
		} catch (IOException e) {
			throw new RuntimeException("File routes.properties not found");
		}
	}
	
	@Override
	protected String[] getURIsFor(Method javaMethod, Class<?> type) {
		String[] uris = super.getURIsFor(javaMethod, type);
		if(type.isAnnotationPresent(Routed.class) || javaMethod.isAnnotationPresent(Routed.class)) {
			String newPath = properties.getProperty(type.getSimpleName() + "." + javaMethod.getName());
			return newPath.split("\\s*,\\s*");
		}
		return uris;
	}
}