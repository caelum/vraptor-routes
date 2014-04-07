package br.com.caelum.vraptor.routes;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.environment.Property;
import br.com.caelum.vraptor.http.route.PathAnnotationRoutesParser;
import br.com.caelum.vraptor.http.route.Router;

@Specializes
public class CustomRouterParser extends PathAnnotationRoutesParser {
	
	private Properties properties;
	@Inject @Property private String routesFileName;

	/**
	 * @deprecated CDI eyes only
	 */
	protected CustomRouterParser() {
	}
	
	@Inject
	public CustomRouterParser(Router router) {
		super(router);
	}
	
	@PostConstruct
	public void postConstruct() {
		properties = new Properties();
		try {
			String routesname = null;
			if(routesFileName != null) {
				routesname = routesFileName;
			}else{ 
				routesname = "/routes.properties";
			}
			properties.load(getClass().getResourceAsStream(routesname));
		} catch (IOException e) {
			throw new RuntimeException("File routes.properties not found");
		}
	}
	
	@Override
	protected String[] getURIsFor(Method javaMethod, Class<?> type) {
		String[] uris = getRawUris(javaMethod);
		if(uris.length > 0) {
			String[] routes = new String[uris.length];
			for (int i = 0; i < routes.length; i++) {
				if(uris[i].matches("^\\{.*\\}$")) {
					String uri = uris[i].replaceAll("\\{|\\}", "");
					routes[i] = properties.getProperty(uri);
				} else {
					routes[i] = uris[i];
				}
			}
			return routes;
		}
		return super.getURIsFor(javaMethod, type);
	}

	private String[] getRawUris(Method javaMethod) {
		if (javaMethod.isAnnotationPresent(Path.class)){
			return javaMethod.getAnnotation(Path.class).value();
		}
		return getUris(javaMethod);
	}
}