# Firenze DI

*DI* 是一种让我们能够更加优雅地创建和管理对象以及对象依赖的一种模式。

## Container

通过 `com.jiahao.di.FContainer` 可以管理需要注入的类，比如有一个类 `Car`：

```java
public class Car {
  public Car () {}
}
```

通过 `com.jiahao.di.FContainer` 的 `register()` 方法可以将其注册到容器中：

```java
com.jiahao.di.FContainer container = new com.jiahao.di.FContainer();
container.register(Car.class);
```

此时 `Car` 类就已经被 `com.jiahao.di.FContainer` 所管理了，然后当我们想获取 `Car` 类的实例时，我们
不必再使用 `Car car = new Car()` 的形式来实例化，而是可以直接通过 `com.jiahao.di.FContainer` 的 
`getInstance()` 方法即可拿到 `Car` 类对应的实例：

```java
Car car = container.getInstance(Car.class);
```

如果想获取所有 `interface` 的实现类，则需先将对应 `interface` 注册到容器中，然后使用 `com.jiahao.di.FContainer`
的 `getInstances()` 方法 ，将相应 `interface` 传递给该方法，则 `getInstances()` 方法会返回该
接口的所有实现类的实例， 比如 `BMW` 和 `Benz` 都是基于 `interface Car` 的实现类：

```java
public interface ICar {
  public String getName();
}

public class BMW implements ICar {
  private static final String name = "BMW";

  @Override
  public String getName() {
    return name;
  }
}

public class Benz implements ICar {
  private static final String name = "Benz";

  @Override
  public String getName() {
    return name;
  }
}
```

则当使用 `getInstances()` 方法获取已经在容器中注册过的 `ICar.class` 时，可以得到所
有 `interface ICar` 的实现类的实例：

```java
List<Car> carsList = getInstance(ICar.class);
```
此时拿到的 `carsList` 列表的长度应为 2，因为其中包含 `BMW` 和 `Benz` 两个类的实例。

但如果尝试在容器中拿到一个没有注册的类的实例，那么容器会抛出异常：

```java
container.getInstance(Bike.class);
// Error: Bike is not registered in the container
```

## @Inject

`@Inject` 可用来标识可注入的 *构造器*，比如：

```java
public class Car {
  private Engine engine;
  
  @Inject
  public Car(Engine engine) {
    this.engine = engine;
  }

  public Engine getEngine() {
    return engine;
  }
}
```

通过上述方式对 `Car` 构造函数标注上 `@Inject` 后，`Engine` 类也会注册到容器中去。
从容器中获取 `Car` 的实例时，`Engine` 也会被对应的实例化：

```java
container.register(Car.class);
Car car = container.getInstance(Car.class);
assertEquals(car.getEngine().class, Engine.class);
```

如果你尝试不使用 `@Inject` 在容器中注册带有参数的构造函数的类的话，容器会抛出异常：

```java
public class Car {
  private Engine engine;
  
  public Car(Engine engine) {
    this.engine = engine;
  }

  public Engine getEngine() {
    return engine;
  }
}

container.register(Car.class);
// Error: Car cannot be registered in container
```

如果你在有多个重载构造函数的类中使用 `@Inject` 注解，那么容器也会抛出异常：

```java
public class Car {
  private Engine engine;
  private Seat seat;
  
  @Inject
  public Car(Engine engine) {
    this.engine = engine;
  }
  
  @Inject
  public Car(Seat seat) {
    this.seat = seat;
  }

  public Engine getEngine() {
    return engine;
  }

  public Seat getSeat() {
    return seat;
  }
}

container.register(Car.class);
// Error: Cannot register the Car which has multiple constructor methods
```

## @Qualifier

通过 `@Qualifier` 注解，可以指明具体地实现类，比如：

```java
public class Car {
  private Engine engine;
  
  @Inject
  public Car(@V8 Engine engine) {
    this.engine = engine;
  }
  
  public Engine getEngine() {
    return engine;
  }
}

@V8
public class V8Engine implements Engine {
  private final String name = "V8";

  public String getName() {
    return name;
  }
}

@Documented
@Retention(RUNTIME)
@Qualifier
public @interface V8 {}
```

在上面的例子中，我们定义了 `@V8` 注解，并使用 `@V8` 标注了实现类以及要注入的地方，这样
在调用 `Car` 的 `getEngine()` 时，就能得到正确的 `Engine` 了：

```java
Car car =  getInstance(Car.class);
Engine engine = car.getEngine();
engine.getName(); // Output: V8
```

## @Named

该注解功能与 `@Qualifier` 类似，通过 `@Named` 注解可以以字符串的形式指明具体地实
现类，比如：

```java
public class Car {
  private Engine engine;
  
  @Inject
  public Car(@Named("V12") Engine engine) {
    this.engine = engine;
  }
  
  public Engine getEngine() {
    return engine;
  }
}

@Named("V12")
public class V12Engine implements Engine {
  private final String name = "V12";

  public String getName() {
    return name;
  }
}
```

## @Singleton

使用 `@Singleton` 标注的实现类，在整个应用中只会存在一个实例，比如：

```java
public class Car {
  private Brand brand;
  
  @Inject
  public Car(Brand brand) {
    this.brand = brand;
  }
  
  public Brand getBrand() {
    return brand;
  }
}

@Singleton
public class Brand {
  private static final String name = "Audi";

  public Brand() {
  }
  
  public String getName() {
    return name;
  }
}
```

如果实例化两个 `Car` 的实例，两个实例的地址在内存中不同，但其中的 `brand` 地址是相同的。
