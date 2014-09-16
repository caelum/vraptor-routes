package br.com.caelum.vraptor.routes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.http.route.PathAnnotationRoutesParser;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Alternative
@Priority(Interceptor.Priority.LIBRARY_BEFORE+10)
public class CustomRouterParser extends PathAnnotationRoutesParser {
	
	private Properties properties = new Properties();
	private final Environment environment;
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected CustomRouterParser() {
		this(null, null);
	}
	
	@Inject
	public CustomRouterParser(Router router, Environment environment) {
		super(router);
		this.environment = environment;
	}
	
	@PostConstruct
	public void postConstruct() {
		try {
			String routesname = environment.get("routesFileName", "/routes.properties");
			properties.load(getClass().getResourceAsStream(routesname));
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