package br.com.caelum.vraptor.routes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.routes.annotation.Routed;

@RunWith(MockitoJUnitRunner.class)
public class CustomRouterParserTest {
	
	private static final Class<?> clazz = FakeController.class;
	
	@Mock private Router router;
	private CustomRouterParser parser;
	@Mock private Environment environment;

	@Before
	public void setUp() {
		when(environment.get(Mockito.eq("routesFileName"), Mockito.anyString())).thenReturn("/routes.properties");
		parser = new CustomRouterParser(router, environment);
		parser.postConstruct();
	}
	
	@Test
	public void shouldProcessCorrectlyNonAnnotatedClasses() throws Exception {
		Class<?> clazz = AnotherFakeController.class;
		Method method = clazz.getDeclaredMethod("mySimpleMethod");
		String[] urIsFor = parser.getURIsFor(method, clazz);
		assertEquals("/mySimplePath", urIsFor[0]);
	}
	
	@Test
	public void shoulAllowExtractRouteOfASingleMethod() throws Exception {
		Class<?> clazz = AnotherFakeController.class;
		Method method = clazz.getDeclaredMethod("myPostMethod");
		String[] urIsFor = parser.getURIsFor(method, clazz);
		assertEquals("/fake/custom/post", urIsFor[0]);
	}
	
	@Test
	public void shoulGetTheUrlAccordingToTheClassAndMethodName() throws Exception {
		Method method = clazz.getDeclaredMethod("fakeMethod");
		String[] urIsFor = parser.getURIsFor(method, clazz);
		assertEquals("/fake/custom/path", urIsFor[0]);
	}
	
	@Routed
	static class FakeController {
		@Get
		public void fakeMethod() {
		}
	}
	
	static class AnotherFakeController {
		@Path("/mySimplePath")
		public void mySimpleMethod() {
		}
		@Post @Routed
		public void myPostMethod() {
		}
	}
}