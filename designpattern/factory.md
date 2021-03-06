# 工厂模式

## 1，简介
### 1.1，工厂模式是什么
**定义：**
> 在基类中定义创建对象的一个接口，让子类决定实例化哪个类。
工厂方法让一个类的实例化延迟到子类中进行。

### 1.2，为什么要有工厂模式

> - 解耦，将对象的创建和使用过程分开，即当class A想调用class B，其实A只是使用B的方法，B的实例化过程，则可以交给工厂类来完成。
> - 降低重复代码，如果创建对象B的过程比较复杂的化，就需要一定的代码量，而且可能很多地方都会用到，就会产生很多重复的代码。若放入工厂类中统一管理，则减少了重复代码，而且方便对B的创建过程的维护。
> -  维护成本降低，创建过程都由工厂统一管理，当业务逻辑发生变化后，不需要找到所有的实例化B的地方去修改，只需要在工厂中修改即可，从而降低维护成本。

### 1.3，适用场景
> - 对象的创建过程/实例化准备工作很复杂，需要初始化很多参数。例如数据库连接
> - 类本身有很多子类，创建过程中业务很容易发生改变，或者类的调用容易发生法改变。

## 2，工厂模式分类及实现

### 2.1，简单工厂模式
**简单工厂模式**
此模式不在23中常用的设计模式中，只能算是工厂模式的一个特殊实现。此模式在实际中的应用很少，只适应很简单的情况。
> 简单工厂模式还违背了**开闭原则**，每次添加新的功能，都要在switch-case或者if—else语句中增加分支，修改代码。

#### 2.1.1，简单工厂模式的三种角色
> - 抽象产品角色(Product)：简单工厂模式所创建的所有对象的弗雷，负责描述所有类共有的接口。
> - 具体产品角色(Concrete Product)：简单工厂模式所创建的目标对象，都是对应角色类的实例。
> - 工厂角色(Factory)：负责创建所有具体产品角色类的实例，是简单工厂模式的核心，可以被外界直接调用，从而实例化产品的对象。

### 2.1.2，简单工厂对象实现
创建一个可以生产手机的工厂，可以生产apple，huawei等，每个手机都有一个可以查询手机类型的方法。

**抽象产品角色：**
```
public interface Phone {//抽象接口类，abstract phone类
    public void getPhoneName();
}
```

**具体产品角色：**
华为手机：
```
public class HuaWei implements Phone {
    @Override
    public void getPhoneName() {
        System.out.println("HuaWei produced!");
    }
}
```
Apple手机:
```
public class Apple implements Phone {//具体产品类concrete product
    @Override
    public void getPhoneName() {
        System.out.println("iPhone produced!");
    }
}
```

**工厂角色：**
```
public class PhoneFactory {//工厂类

    public static Phone makePhone(String phone) {
        if ("Apple".equals(phone)) {
            return new Apple();
        } else if ("HuaWei".equals(phone)) {
            return new HuaWei();
        } else {
            return null;
        }
    }
}
```

**测试方法：**
```
public class FactoryDemo {

    public static void main(String[] args) {
        Phone apple = PhoneFactory.makePhone("Apple");
        Phone huaWei = PhoneFactory.makePhone("HuaWei");
        apple.getPhoneName();
        huaWei.getPhoneName();
    }
}
```

**输出：**
```
iPhone produced!
HuaWei produced!
```

**反射机制满足开闭原则：**
```
public class PhoneFactory {//工厂类
  public static Object makePhone(Class<? extends Phone> clazz) {
      Object object = null;
      try {
          object = Class.forName(clazz.getName()).newInstance();
      } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
          e.printStackTrace();
      }
      return object;
  }
}

```
**测试类：**
```
public class FactoryDemo {
  public static void main(String[] args) {
    Apple apple1 = (Apple) PhoneFactory.makePhone(Apple.class);
    HuaWei huaWei1 = (HuaWei) PhoneFactory.makePhone(HuaWei.class);
    apple1.getPhoneName();
    huaWei1.getPhoneName();
  }
}
```

**输出：**
```
iPhone produced!
HuaWei produced!
```

### 2.1.3，简单工厂适用场景
> - 适用于创建的对象较少的情况
> - 客户端不关心工厂对象实例化的过程
> - Spring中，bean工厂是用的简单工厂模式来实现的

### 2.1.4，简单工厂优缺点
**优点：**
> - 此类中有必要的判断条件，可以决定在什么时候创建哪一种类的实例化。
> - 客户端仅仅使用对象，而不关心创建过程，实现了对责任的分割
> - 根据外界给定的信息，工厂类决定具体实例化哪个对象

**缺点：**
> - 当产品的多层级结构比较复杂的时候，工厂类无法满足需求
> - 不利于维护，一旦增加或者删除产品，需要修改整个工厂类，扩展困难，违背了开闭原则
> - 简单工厂使用了静态工厂方法，无法由子类集成。


### 2.2，工厂方法模式
**工厂方法模式**，也可以叫做工厂模式、多态工厂模式、虚拟构造器模式。应该是工厂模式中使用最多的模式。
> 定义一个用于创建对象的接口，让子类决定实例化哪一个类。工厂方法使一个类的实例化延迟到其子类中进行。

#### 2.2.1，为什么要有工厂方法模式
由于简单工厂模式的上述缺点，因此，工厂方法模式就解决上述问题。
> - 实例化过程，工厂方法模式把具体的产品的实例化推迟到具体工厂中进行，此事工厂类不负责具体产品的实例化，只是给出具体工厂必须实现的接口。
> - 可维护性，若添加新的产品或者删除产品，直接删除工厂子类或者添加新的工厂子类就可以了，符合开闭原则原则。

#### 2.2.2，工厂方法模式四种角色
角色 | 关系 | 作用 
:-: | :-: | :-: 
抽象产品（product） | 具体产品的父类 | 定义具体的产品的公共接口
具体产品（concrete product）|抽象产品的子类，工厂类创建的目标类 | 定义生产的具体产品| 
抽象工厂（factory）|具体工厂的子类 |定义具体工厂的公共接口 |
具体工厂（concrete）| 抽象工厂的子类，被外界调用来实例化具体产品类| 定义具体工厂，实现抽象工厂方法创建产品的实例|


#### 2.2.3，工厂方法模式实现
**抽象产品：** 手机类
```
public abstract class Phone {
    public void getName(){
    }
}
```
**具体产品：**

**Apple手机：**
```
public class Apple extends Phone {
    @Override
    public void getName() {
        System.out.println("Apple phone");
    }
}
```
**Huawei手机：**
```
public class HuaWei extends Phone {

  @Override
  public void getName() {
    System.out.println("HuaWei");
  }
}

```

**抽象工厂：** 手机制造工厂
```
public interface Factory {
    public Phone makePhone();
}
```
**具体工厂：**
**Apple手机工厂：**
```
public class AppleFactory implements Factory {

    @Override
    public Phone makePhone() {
        return new Apple();
    }
}
```
**Huawei手机工厂：**
```
public class HuaWeiFactory implements Factory {
  @Override
  public Phone makePhone() {
    return new HuaWei();
  }
}
```

**测试：**
```
public class FactoryDemo {
  public static void main(String[] args) {
    AppleFactory appleFactory = new AppleFactory();
    HuaWeiFactory huaWeiFactory = new HuaWeiFactory();
    appleFactory.makePhone().getName();
    huaWeiFactory.makePhone().getName();
  }
}
```
**输出：**
```
Apple phone
HuaWei
```
#### 2.2.4，工厂方法模式的优缺点
**优点：**
> - 主要针对简单工厂模式中的问题而设计，满足了开闭原则（OCP）。
> - 维护性很好，增加或者删除产品，只需要修改对应的具体工厂和具体产品类即可。

**缺点：**
> - 如果某具体产品需要进行修改，可能需要工厂类
> - 此外，若要增加新的产品类，就要增加对应的产品工厂，增加额外的开发量。

#### 2.2.5，工厂方法模式的使用场景
- 一个类实例化的过程中，不需要知道具体的产品类名，只需要知道对应的工厂即可。
- 工厂模式中，通过面向对象的多态性和里氏代换原则，来创建产品。程序运行过程中，子类对象覆盖父类对象，从而使得系统更容易拓展

**工厂方法模式在Spring中的应用**
> 待补充
>

### 2.3，抽象工厂模式
抽象工厂模式，在实际的生产中，一个工厂不仅仅创建一种产品，而是可以创建一组产品，例如Apple的工厂不仅仅生产iphone，还会生产airpod等。
**定义：**
> 提供一个创建一系列相关或者依赖对象的接口，而无需指定他们具体的类。

#### 2.3.1，为什么要有抽象工厂模式
> - 由于工厂模式为每个工厂只实例化一种具体类，会导致系统中工厂类过多，增加了系统的开销
> - 将一些相关的具体类组成一个具体类族，由同一个工厂来统一生产。这也就是抽象工厂模式的基本思想

**见下图：**
![abstractfactory][abstractfactory]

> - 对于同一个类型的产品，称为一个产品等级，如手机这个产品等级，可以有不同的产品，apple，huawei等。
> - 对于同一组类型的产品，称为一个产品族，如apple工厂可以生产手机，耳机等。

#### 2.3.2，抽象工厂方法模式四种角色
角色 | 关系 | 作用 
:-: | :-: | :-: 
抽象产品（product） | 具体产品的父类 | 定义具体的产品的公共接口
具体产品（concrete product）|抽象产品的子类，工厂类创建的目标类 | 定义生产的具体产品| 
抽象工厂（factory）|具体工厂的子类 |定义具体工厂的公共接口 |
具体工厂（concrete）| 抽象工厂的子类，被外界调用来实例化具体产品类| 定义具体工厂，实现抽象工厂方法创建产品的实例|

#### 2.3.3，实现
同一个产品等级中，共有一个抽象产品类和若干个具体产品等级。
**抽象产品类 Phone：**
```
public abstract class Phone {
    public void getName() {

    }
}
```

**具体产品类——Apple**
```
public class Apple extends Phone {

    @Override
    public void getName() {
    System.out.println("Apple Phone");
    }
}
```
**具体产品类——HuaWei：**
```
public class HuaWei extends Phone {
    @Override
    public void getName(){
        System.out.println("Hua wei Phone");
    }
}
```

**抽象产品类 Headset：**
```
public class Headset {
    public void getName() {

    }
}
```
**具体产品类——Airpod**
```
public class AirPod extends Headset {

    @Override
    public void getName() {
        System.out.println("Apple Air pod");
    }
}
```
**具体产品类——HuaWeiHeadset：**
```
public class HuaWeiHeadset extends Headset {
    @Override
    public void getName() {
        System.out.println("Hua wei Headset");
    }
}
```
同一个产品族中，共有一个抽象工厂类和若干个具体工厂类。具体工厂中可以实例化多种具体产品类。
**抽象工厂类：**
```
public interface Factory {
    public Phone makePhone();

    public Headset makeHeadset();
}
```
**具体工厂类——Apple工厂：**
```
public class AppleFactory implements Factory {
    @Override
    public Phone makePhone() {
        return new Apple();
    }

    @Override
    public Headset makeHeadset() {
        return new AirPod();
    }
}
```
**具体工厂类——HuaWei工厂：**
```
public class HuaWeiFactory implements Factory {
    @Override
    public Phone makePhone() {
        return new HuaWei();
    }

    @Override
    public Headset makeHeadset() {
        return new HuaWeiHeadset();
    }
}
```
**测试：**
```
public class FactoryDemo {
    public static void main(String[] args) {

        AppleFactory appleFactory = new AppleFactory();
        Phone iPhone = appleFactory.makePhone();
        Headset airPod = appleFactory.makeHeadset();

        HuaWeiFactory huaWeiFactory = new HuaWeiFactory();
        Phone huaWei = huaWeiFactory.makePhone();
        Headset huaWeiHeadset = huaWeiFactory.makeHeadset();

        iPhone.getName();
        airPod.getName();
        huaWei.getName();
        huaWeiHeadset.getName();
    }
}
```

**输出：**
```
Apple Phone
Apple Air pod
Hua wei Phone
Hua wei Headset
```

#### 2.3.4，优缺点
**优点：**
> - 隔离了具体类的实例化过程，使用者不用关心具体的实例化。
> - 抽象工厂可以实例化一族的具体产品
> - 加入新的产品族比较简单，符合**开闭原则**

**缺点：**
> - 若一个工厂需要可以实例化新的产品，则需要修改具体工厂的代码，不符合**开闭原则**


#### 2.3.5，适用场景

 - 和工厂方法一样客户端不需要知道它所创建的对象的类。
 - 需要一组对象共同完成某种功能时，并且可能存在多组对象完成不同功能的情况。（同属于同一个产品族的产品）
 - 系统结构稳定，不会频繁的增加对象。（因为一旦增加就需要修改原有代码，不符合开闭原则）




## 3，总结
**工厂模式在Spring中的应用**
- 工厂模式：在各种BeanFactory以及ApplicationContext创建都用到了。



[abstractfactory]:./asserts/abstractfactory.png