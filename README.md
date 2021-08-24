# Firenze DI

*DI* 是一种让我们能够更加优雅地创建对象和对象依赖的一种模式。

## Container

通过 `FContainer` 可以管理需要注入的类，比如有一个类 `Car`：

```java
public class Car {
  public Car () {}
}
```

通过 `FContainer` 的 `register()` 方法可以将其注册到容器中：

```java
FContainer container = new FContainer();
container.register(Car.class);
```

然后通过 `getInstance()` 方法即可拿到 `Car` 类对应的实例：

```java
Car car = container.getInstance(Car.class);
```

如果你尝试在容器中获取一个没有注册的类，那么容器会抛出异常：

```java
container.getInstance(NotExisted.class);
// Error: NotExisted class is not registered in the container
```

## @Inject

用来标识可注入的 *构造器*，比如：

```java
public class Car {
  public Engine engine;
  
  @Inject
  public Car(Engine engine) {
    this.engine = engine;
  }
}
```

通过上述方式标注 `Car` 构造函数后，`Engine` 也会对应注册到容器中。 
从容器中获取 `Car` 的实例时，`Engine` 也会被对应的实例化：

```java
container.register(Car.class);
Car car = container.getInstance(Car.class);
assertEquals(car.engine.class, Engine.class);
```

如果你尝试不使用 `@Inject` 在容器中注册带有参数的构造函数的类的话，容器会抛出异常：

```java
public class Car {
  public Engine engine;
  
  public Car(Engine engine) {
    this.engine = engine;
  }
}

container.register(Car.class);
// Error: Car class cannot be registered in container
```

如果你在有多个重载构造函数的类中使用 `@Inject` 注解，那么容器也会抛出异常：

```java
public class Car {
  public Engine engine;
  public Seat seat;
  
  @Inject
  public Car(Engine engine) {
    this.engine = engine;
  }
  
  @Inject
  public Car(Seat seat) {
    this.seat = seat;
  }
}

container.register(Car.class);
// Error: Cannot register the Car class which has multiple constructor methods
```

## @Qualifier

通过 `@Qualifier` 可以指明具体地实现类，比如：

```java
class Car {
  public Engine engine;
  
  @Inject
  public Car(@V8 Engine engine) {
    this.engine = engine;
  }
}

@V8
public class V8Engine implements Engine {
  private final String name = "V8";

  @Override
  public String getName() {
    return name;
  }
}

@Documented
@Retention(RUNTIME)
@Qualifier
public @interface V8 {}
```

## @Named

通过 `@Named` 可以指明具体地实现类，比如：

```java
public class Car {
  public Engine engine;
  
  @Inject
  public Car(@Named("V12") Engine engine) {
    this.engine = engine;
  }
}

@Named("V12")
public class V8Engine implements Engine {
  private final String name = "V12";

  @Override
  public String getName() {
    return name;
  }
}
```

## @Singleton

使用 `@Singleton` 标注的实现类在整个应用中只会存在一个实例，比如：

```java
public class Car {
  public Brand brand;
  
  @Inject
  public Car(@Singleton Brand brand) {
    this.brand = brand;
  }
}
```

如果实例化两个 `Car` 的实例，两个实例的地址在内存中不同，但其中的 `brand` 地址是相同的。
