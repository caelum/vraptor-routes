vraptor-routes
==============

To use it you just need to create an simple `routes.properties` file with 
your application routes:

```
SomeController.method1 = /my/beauthy/route
SomeController.method2 = /my/{param}/route
```

And add the `@Routed` annotation at your `controller` or `controller methods`:

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

If you want to use other file you can also configure the file name on `environment.properties`:

```
routesFileName = routes_pt-BR.properties
```

You can use the `@Routed` annotation with methods containing priority normally, but you should leave the `@Path` value empty (plugin will always consider the routed url).

```java
@Routed
@Path(priority=Path.HIGH, value="")
public void myAmbiguousMethod(){ //... }
```




