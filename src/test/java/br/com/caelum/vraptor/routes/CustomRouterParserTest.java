package br.com.caelum.vraptor.routes;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.routes.CustomRouterParser;

@RunWith(MockitoJUnitRunner.class)
public class CustomRouterParserTest {
	
	private static final Class<FakeController> clazz = FakeController.class;
	
	@Mock private Router router;
	private CustomRouterParser parser;

	@Before
	public void setUp() {
		parser = new CustomRouterParser(router);
		parser.postConstruct();
	}
	
	@Test
	public void testName() throws Exception {
		String[] urIsFor = parser.getURIsFor(getMethod("index3"), clazz);
		Assert.assertEquals("/oie", urIsFor[0]);
	}
	@Test
	public void testName2() throws Exception {
		String[] urIsFor = parser.getURIsFor(getMethod("index4"), clazz);
		Assert.assertEquals("/liliurl", urIsFor[0]);
		Assert.assertEquals("/lolourl", urIsFor[1]);
	}
	
	static class FakeController {
		
		@Get("/lala")
		public void index2() {
		}
		
		@Path("{lele}")
		public void index3() {
		}
		
		@Post({"{lili}", "{lolo}"})
		public void index4() {
		}
	}
	
	private Method getMethod(String methodName) throws NoSuchMethodException {
		return clazz.getDeclaredMethod(methodName);
	}
}