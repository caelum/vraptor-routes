vraptor-routes
==============

To use it you just need to create an simple `route.properties` file with 
your application routes:

```
SomeController.method1 = /my/beauthy/route
SomeController.method2 = /my/{param}/route
```

And add the `@Routed` annotation at your `controller` or `controller method`s:

```java
@Controller 
@Routed
public class SomeController {

	@Post 
	public void method1() {
	}

	@Get 
	public void method2() {
	}
}
```

optionally you can also configure the file name on `environment.properties`:

```
routesFileName = routes_pt-BR.properties
```