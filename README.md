# Mock.java使用说明手册



## 简介



> 这是一个仿照Mock.js语法的Java语言使用的假数据生成工具框架。
> 部分方法与类介绍详细可查看JavaDoc文档(推荐先下载下来再看)：[JavaDoc文档](helpDoc/index.html)
>
> 码云生成的在线javaDoc文档：[在线文档](https://apidoc.gitee.com/ForteScarlet/Mock.java/)
>
> 如果存在BUG或者有什么意见、建议，可以通过邮箱`ForteScarlet@163.com`进行反馈或者联系QQ`1149159218`.
> (记得注明身份哦~)
>
> github: [github](https://github.com/ForteScarlet/Mock.java)
>
> gitee : [码云地址](https://gitee.com/ForteScarlet/Mock.java)
>
> 此框架中不仅仅只可以作为假数据获取用，还有一些比较实用的工具类可以拿来单独使用。
>
>  *工具类介绍：<a href="#工具类介绍">工具类介绍</a>
>
> 当前版本：v1.4.2 (部署时间：2019/10/18 22:00)
>
> 最低JDK版本：JDK8
>
> 以下介绍的版本：v1.4.2 (第一版)
>
> *※ 版本更新内容与预期更新计划详见于文档末尾 ： <a href="#更新公告">更新公告</a>*
>
>

## 使用方法
### 安装

在maven项目下，从pom.xml中导入以下地址：
> 最新版本以maven仓库中的地址为准。仓库地址：`https://mvnrepository.com/artifact/io.gitee.ForteScarlet/mock.java`

```xml
<dependency>
    <groupId>io.gitee.ForteScarlet</groupId>
    <artifactId>mock.java</artifactId>
    <version>1.4.2</version>
</dependency>
```



### 使用

> 相信使用过Mock.js的各位大佬应该知道，在使用Mock.js的时候是用的JSON格式的参数。
> 但是，Java可是没法直接识别JSON的啊！
> 所以，我们采用最接近JSON格式的方式：**Map集合**。
>
> 简单来说，就是将一个类的字段根据Mock.js那样的key-value的键值对转化为一个Map<String, Object>对象就好了！我习惯将这种Map对象称为 *字段映射表* 。
>
> 而且作为Java语言，数据类型是必须要多加考虑的问题。我在获取值的时候已经尽可能的增加了容错率，但是还是需要您注意数据类型的问题，请尽可能不要犯下将一个字符串赋值给整数这类难以防范的错误..
>
> 或许感觉上比JSON格式的使用要麻烦一些，但是这也是没有办法的事情嘛！假如您有更好的代替方式，希望您能告诉我 :) 



## 框架中的一些常见"角色"

#### 	参数解析器/任务分配器(ParameterParser)

> ​	负责对用户传入的字段映射(Map集合)进行解析并分配解析任务。
>
> ​	也可以将其理解为 **任务分配器** 。

#### 	字段解析器(FieldParser)

> ​	接收任务分配器分配的任务并对字段和其映射进行解析，并取得字段值获取器(FieldValueGetter)。

#### 	字段值获取器(FieldValueGetter)

> ​	使用字段值的setter方法和字段值获取方法执行者(Invoker)对某个字段进行赋值。

#### 	字段值获取方法执行者(Invoker)

> ​	通过执行一个某种方法得到一个结果。用于获取字段的值。

#### 	假对象(MockObject)

> ​	通过Mock.get(Class<T> clz)方法获取到的返回值，用于获取假对象数据。

### 设置字段映射的方式：

#### 	1·创建对象字段与随机值语法的映射关系(Map<String , Object> 类型的键值对)

>  创建的这个Map，Key值代表了映射的字段名，value值代表了映射语法
> 由于这毕竟与弱引用类型语言不同，所以在设置映射的时候请务必注意字段的数据类型。

​	`Map<String, Object> map = new HashMap<>();`	

#### 	2·添加字段映射 

> 字段映射中，value值所用到的 @函数 可以从 [JavaDoc文档](helpDoc/index.html) 中查阅[**MockUtil**]类中的方法，MockUtil中的全部方法均可作为 @函数 出现在value值中。
>
> **再次提醒，请务必注意对应好字段的字段类型**

```java
map.put("age","@age");
map.put("list|2-3","@title");
map.put("user","@name");  
......
```

> key值中，有三种写法： 
>
> 仅有字段映射、字段映射与整数部分区间参数、字段映射、整数部分区间参数与小数部分区间参数。
>
> 例如如下这么两个字段映射：
>
> ```java
> map.put("money1|10-40.2-4" , 0);
> map.put("money2|10-40.2" , 0);
> ```
>
> 其中，字段名与区间参数之间的分割符为 **|** 符号，左边为字段名，右半边为区间参数。
>
> 区间参数中，整数部分与小数部分用 **.** 符-																																																																																																																												号分割，左半边为整数部分区间参数，右半边为小数部分区间参数。
>
> - ##### 仅有字段映射
>
>   > 任务分配器首先会根据参数(value)的类型分配字段解析器，然后再根据字段类型进行取值。
>   >
>   > 参数类型有一下几种情况：
>   >
>   > - **字符串类型**：如果存在一个或多个@函数，解析@函数并取值，（如果有多个@函数则会尝试对@函数的取值进行加法计算）；如果不存在@函数或@函数不存在于MockUtil中的方法列表则将其会视为普通字符串。
>   > - **整数（Integer）、浮点数（Double）类型**：如果参数为Integer或Double类型，则字段值获取器会直接将此值作为默认值赋给字段。
>   > - **数组或集合**：如果参数是数组或集合类型，字段值获取器会从其中随机获取一个值赋予字段。
>   > - **Map集合**：如果参数是Map集合类型，则任务分配器会对字段的类型进行判断，如果：
>   >   - 字段为Map类型，则直接将此Map作为字段值赋予字段，不做处理。
>   >   - 字段为List<Map>类型，则字段值获取器会将将此Map集合封装至List集合中并返回。
>   >   - 字段为其他任意类型，则任务分配器会将此Map视为此字段类型的字段映射集合进行解析并获取一个实例对象为字段赋值。**(※注：此字段映射同样会被Mock的映射集合记录下来。即嵌套的字段映射不需要单独再进行set了。)**
>   > - **其他任意类型**：如果参数不是上面的任何类型，则字段值获取器会将此参数原样赋值，不做处理。
>
>   ```java
>   //假设以下字段映射的是User类
>   Map<String, Object> map = new HashMap<>();
>   map.put("age1" , "@age");
>   map.put("age2" , 15);
>   map.put("age3" , new Integer(){1,2,3,4});
>   map.put("name1" , "@name");
>   map.put("name2" , "@title(2)");
>   map.put("name3" , "这是一个名字");
>   
>   //下面三个email字段的参数，如果是中文，必须放在单引号或双引号中才会生效，英文不受限制
>   map.put("email1" , "@email('这是中文')");
>   map.put("email2" , "@email('this is english')");
>   map.put("email3" , "@email(this is english)");
>   
>   //下面的friend字段的字段类型是一个Friend类，friendMap是对friend字段的映射，也就是嵌套映射
>   //此friendMap的映射无需单独进行记录
>   map.put("friend" , friendMap);
>   
>   //记录映射
>   Mock.set(User.class, map);
>   //User类的映射被直接记录，可以获取
>   MockObject<User> userMockObject = Mock.get(User.class);
>   //Friend类的映射以嵌套的形式被记录过了，可以直接获取
>   MockObject<Friend> friendMockObject = Mock.get(Friend.class);
>   ```
>
> - ##### 字段映射与仅整数部分的区间参数
>
>   > 参数类型有一下几种情况：
>   >
>   > - **字符串类型**：如果存在一个或多个@函数，解析@函数并取值，（如果有多个@函数则会尝试对@函数的取值进行加法计算）；在存在@函数的情况下，区间参数将被忽略。从`v1.4.2`之后，当字段类型为Object类型（常见于创建Map类型对象）则会根据区间函数创建范围内大小的List集合。（详细见`v1.4.2`更新日志）
>   >
>   >
>   >
>   >   如果不存在@函数或@函数不存在于MockUtil中的方法列表则将其会视为普通字符串，然后根据整数参数区间获取一个随机数值并对此字符串进行重复输出。
>   >
>   > - **整数（Integer）、浮点数（Double）类型**：如果参数为Integer或Double类型，则字段值获取器将区间参数作为方法参数，根据字段的类型使用随机函数获取对应的随机值。
>   >
>   >   例如：
>   >
>   >   ```java
>   >   //age为一个Integer类型的字段，等同于使用了@integer(2,4)函数
>   >   map.put("age|2-4" , 0);
>   >   //money为一个Double类型，等同于使用了@doubles(2,4)函数
>   >   map.put("money|2-4" , 0);
>   >   ```
>   >
>   >   则age将会被赋予一个2-4之间的随机整数(Integer)，money将会被赋予一个2-4之间最大小数位数为0的浮点数(Double)。
>   >
>   > - **数组或集合**：如果参数是数组或集合类型，任务分配器会判断字段的类型分配字段值获取器：
>   >
>   >   - 字段类型为整数或浮点数，则区间参数将会被忽略吗，直接从参数中获取一个随机元素并赋值。
>   >
>   >     *如下所示三种情况的取值是完全相同的：*
>   >
>   >     ```java
>   >     //age为一个Integer类型的字段
>   >     map.put("age|2-4" , new Integer[]{1,2,3});
>   >     map.put("age|2" , new Integer[]{1,2,3});
>   >     map.put("age" , new Integer[]{1,2,3});
>   >     ```
>   >
>   >   - 字段值类型同位数组或集合的时候，会根据区间参数获取一个随机数量，并从传入的参数中获取此数量的随机元素。
>   >
>   > - **Map集合**：如果参数是Map集合类型，则任务分配器会对字段的类型进行判断，如果：
>   >
>   >   - 字段为Map类型，则直接将此Map作为字段值赋予字段且忽略区间参数，不做处理。
>   >   - 字段为List<Map>类型，则字段值获取器会将将此Map集合封装至List集合并根据区间参数重复一个随即数量并返回。
>   >   - 字段为List<? extends Object> 类型，即一个任意泛型的List类型的时候，任务分配器会将此Map视为此泛型类型的字段映射集合进行解析，再根据区间参数获取指定范围内数量的实例对象，并封装为List类型为字段赋值。**(※注：上文提到过，内嵌字段映射同样会被记录。)**
>   >   - 字段为其他任意类型，则任务分配器会将此Map视为此字段类型的字段映射集合进行解析并获取一个实例对象为字段赋值，忽略区间参数。**(※注：上文提到过，内嵌字段映射同样会被记录。)**
>   >
>   > - **其他任意类型**：
>   >
>   >   - 如果字段是list类型或数组类型，则会根区间参数重复输出并为字段赋值。
>   >   - 如果字段类型为其他未知类型，则会忽略区间参数并使用参数值作为默认值赋值。
>
>   ```java
>   map.put("list|2-6" , "@title");
>   map.put("age|10-40" , 2);
>   ```
>
> - 字段映射、整数区间参数和小数区间参数
>
>   > 参数类型有一下几种情况：
>   >
>   > - **字符串类型**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
>   >
>   > - **整数（Integer）、浮点数（Double）类型**：如果字段类型为整数，则会无视小数部分区间参数，与整数区间参数时的情况相同（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
>   >
>   >   如果字段类型为小数，则会根据区间参数获取一个指定区间内的随机小数，例如：
>   >
>   >   ```java
>   >   // money为一个Double类型的字段，此映射等同于使用了@doubles(2,4,2,4)函数
>   >   map.put("money|2-4.2-4" , 0);
>   >   // money为一个Double类型的字段，此映射等同于使用了@doubles(2,4,2)函数
>   >   map.put("money|2-4.2" , 0);
>   >   ```
>   >
>   > - **数组或集合**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
>   >
>   > - **Map集合**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
>   >
>   > - **其他任意类型**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
>



#### 	3·获取假字段封装对象

通过Mock的get方法获取一个已经添加过映射记录的数据

* 首先使用set方法记录类的字段映射

  ```java
  //映射表尽可能是Sting,Object类型的
  Map<String, Object> map = new HashMap<>();
  //添加一个映射
  map.put("age|10-40" , 2);
  //记录类的映射
  //1、使用javaBean封装
  Mock.set(User.class , map);
  //2、或者直接使用Map类型，不再需要javaBean的class对象，但是需要指定一个映射名
  Mock.set("userMap", map);
  ```


  *  然后使用get方法得到假对象封装类

```java
//已经记录过User类的映射,获取封装类
//1、如果是使用的javaBean记录的，使用javaBean获取
MockObject<User> mockObject = Mock.get(User.class);
//2、或者你之前是使用map记录的，使用记录时保存的映射名获取
//注：MockMapObject 对象实现了MockObject接口
MockMapObject mockMapObject = Mock.get("userMap");
```

  * 根据MockObject中提供的API来获取你所需要的结果：

    ```java
    Optional<T> get();
    ```

    > 获取一个结果，并使用Optional类进行封装。

    ```java
    T getOne();
    ```

    > 获取一个结果

    ```java
    List<T> getList(int num);
    ```

    > 获取指定数量的多个结果，返回List集合

    ```java
    List<R> getList(int num , Function<T, R> mapper);
    ```

    > 获取指定数量的多个结果，并根据给定规则进行转化，返回List集合

    ```java
    Set<T> getSet(int num);
    ```

    >获取指定数量的多个结果，返回Set集合

    ```java
    Set<R> getSet(int num , Function<T, R> mapper);
    ```

    > 获取指定数量的多个结果，并根据给定规则进行转化，返回Set集合

    ```java
    Map<K,V> getMap(int num , Function<T,K> keyMapper , Function <T,V> valueMapper);
    ```

    > 获取指定数量的多个结果，并根据给定规则转化为Map集合

    **※ 自1.3版本之后，我优化了`MockObject`接口内部结构，并增加了大量parallel方法与collect方法，您现在可以在1.3版本中更加灵活的对数据进行转化，或者根据数据量的需求自行决定是否需要使用并行线程进行对象创建。**

## 自定义@函数

> 有时候，我提供的MockUtil中的方法可能无法满足您的需求，那么这时候，就需要一个可以对@函数进行扩展、加强的窗口。在v1.1版本中，我添加了这个功能。(这个功能测数量很少，可能会存在很多bug)

### 1· 获取自定义@函数加载器

```java
//获取@函数加载器
MethodLoader methodLoader = Mock.mockMethodLoader();
```

函数加载器支持链式加载，也支持一次性加载

链式：

```java
LoadResults loadResults = methodLoader
				//添加指定类中的指定方法名的方法
                .append(Demo1.class, "testMethod")
    			//添加指定类中的多个指定方法名的方法
                .appendByNames(Demo2.class, new String[]{"method1" , "method2"})
    			//添加指定类中的多个符合指定正则回则的方法
                .appendByRegex(Demo3.class, "[a-zA-Z]+")
    			//还有很多...敬请查阅API文档
                .load();
```

> 使用链式加载的时候，请务必记住在结尾使用load()进行加载，否则方法集将无法被加载，而是一直留存在等待区。

非链式：

```java
methodLoader.add(Demo1.class, "testMethod");
```

通过以上代码可以发现，加载完成后都会有一个` LoadResults` 类作为返回值，这个类是在方法加载后的一个加载报告封装类，通过`LoadResults` 可以获取到刚刚加载的方法谁成功了，谁失败了，失败了的方法为什么失败等信息：

```java
 		
Map<Boolean, Set<Method>> map = loadResults.loadResults();//加载的方法集根据成功与否分组
Set<Method> successMethods = loadResults.loadSuccessResults();//加载成功的方法集
Map<Method, Exception> whyFailMap = loadResults.whyFail();//加载失败的方法以及抛出的异常
int successNum = loadResults.successNums();//成功的个数
int failNum = loadResults.failNums();//失败的个数
```

假若加载成功后，则此方法便可以直接在映射中直接用@开头作为使用@函数使用了~



# 注解形式映射

1.4版本之后我提供了两个可以使用在字段上的注解：`@MockValue` 和 `@MockArray`

## @MockValue

使用在类的字段上，仅有一个参数：

```java
    /**
     * 映射值，如果为空则视为无效
     */
    String value();
```



用来指定此字段的映射值。例如：

```java
public class User {
    @MockValue("@name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```





## @MockArray

使用在类的字段上，有连个参数，必填参数仅有一个：

```java
	/**
     * 数组参数, 必填参数
     */
    String[] value();

    /**
     * 类型转化器实现类，需要存在无参构造
     * 默认不变
     */
    Class<? extends ArrayMapper> mapper() default ArrayMapperType.ToString.class;
```

其中，`mapper()`参数可选，其类型为`ArrayMapper`接口的的实现类，用于指定将字符串数组，也就是`value()`中的值进行转化的规则。此参数默认为不进行转化，即转化为字符串类型。

`ArrayMapper`接口中有两个抽象方法：

```java
	/**
     * 给你一个数组长度，返回一个数组实例的function，用于数组的实例化获取
     * @return 数组实例获取函数，例如：Integer[]::new; 或者 size -> new Integer[size];
     */
    IntFunction<T[]> getArrayParseFunction();

	/**
	 * 将字符串转化为指定类型
	 */
	T apply(String t);

```

在对`ArrayMapper`接口进行实现的时候，请务必保留下无参构造用于对其进行实例化。



对于一些比较常见的类型转化，我提供了几个已经实现好的实现类。这些实现类以内部类的形式存在于`ArrayMapperType`接口中。

- `ArrayMapperType.ToString.class` 

  转化为字符串类型，即不进行转化

- `ArrayMapperType.ToInt.class`

  转化为Integer类型

- `ArrayMapperType.ToLong.class`

  转化为Long类型

- `ArrayMapperType.ToDouble.class`

  转化为Double类型

例如：

```java
public class User {
    @MockArray(value = {"1", "2", "3"}, mapper = ArrayMapperType.ToInt.class)
    private int age;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```





## 使用

使用也很简单，我在`Mock`中增加了4个方法，2个`se`t方法 2个`reset`方法。

```java
	/**
     * 通过注解来获取映射
     */
    public static <T> void set(Class<T> objClass);


	/**
     * 通过注解来获取映射, 并提供额外的、难以用注解进行表达的映射参数
     */
    public static <T> void setWithOther(Class<T> objClass, Map<String, Object> other);	

	/**
     * 通过注解来获取映射
     */
    public static <T> void reset(Class<T> objClass);

	/**
     * 通过注解来获取映射, 并提供额外的、难以用注解进行表达的映射参数
     */
    public static <T> void resetWithOther(Class<T> objClass, Map<String, Object> other);


```





## 注意事项

### 注解优先级

假如你在同一个字段上同时使用了两个注解，则会优先使用`@MockValue`;



### 额外映射

可以发现，4个方法中各有一个方法需要提供额外参数，他会在注解映射创建完毕后进行添加，也就是假如额外参数和字段中有冲突的键，则额外参数的值将会覆盖注解映射值。







# 工具类介绍

## 引言

如果你觉得，Java没有必要获取假数据，认为我这个所谓"框架"就是一个垃圾，那么何尝不来看看内部自带的几个工具类呢？

(工具类的详细介绍请查看[JavaDoc文档](helpDoc/index.html) 中包路径 *com.forte.util.utils* 下的类  )

## 值得一提的：

----

##### FieldUtils - 字段相关反射工具类

这是工具类包下最大的一个工具类，至少体积是最大的，用到的次数也是最大的。

这个工具类在对字段进行取值的时候，使用了单层级字段、多层级字段的双缓存机制，不论你是单层字段，还是多层对象嵌套的字段，只需获取一次后，再次获取的效率与直接调用getter方法无异。某天做过5亿数据量的字段值获取测试，经过缓存后获取效率超大幅的提高 。（ *关于测试的结果可以查看[JavaDoc文档](helpDoc/index.html) 中此类的文档注释。*）

至少它对这个框架来讲用处很大，但是一般情况下来讲，用反射频繁获取字段值的情况并不多见呢..

##### MockUtil - 随机值获取工具类

这是本框架的核心工具类之一，所有的@函数都是基于动态获取MockUtil中的方法而实现的。（ ~~*后期考虑可以使使用者对随机值方法进行横向扩展，即自定义@函数。*~~ ( 已实现 )）

##### RandomUtil - 随机值、数值区间工具类

它与上面的MockUtil 最大的不同点是它主要是获取一些范围区间内的随机值、随机码、浮点数的小数保留等，更偏向于一种对数据、数值的操作。

##### SingleFactory - 单例工厂

一个简单的、以乐观锁为原理的线程安全的单例工厂，提供丰富的API来设置、保存、替换单例对象。

###### 还有其他...



## 更新公告

## v1.4.2(2019.10.18)
修复机制：当生成Map类型的值的时候，假如字段映射类似于这种格式：
```json
{
    "a": {
        "array|5-10":"@name"
    }
}
```
更新之前，`array`字段将会忽略区间参数`5-10`直接根据`@name`生成随机姓名，更新后则将会生成长度在5-10之间的list集合。

修复当参数为List类型的时候，作为参数List没有被copy的bug。

距离功能写完到现在，我意识到了曾经的自己十分喜欢使用异步并行流遍历，然而这不一定是效率最高的，
所以在内部我修改了两处我看到的并行流遍历代码并修改为了单线程。


## v1.4(2019.8.26)

提供两个注解以实现注解形式的映射。(测试量较少不知道有没有bug)



### v1.3(2019.7.12)

优化`MockObject`接口内部接口，增加大量`parallel`(并行流)方法与`collect`方法。

`parallel`相关方法将会在您创建对象的时候使用并行线程进行创建，当您需求的数据量较大的时候，此系列方法将会相对于原本的串行方法更加有效率。

`collect`相关方法将会提供大量参照于`Stream`中的`collect`方法而制定的方法。加入您对于`Stream`中的`collect`方法十分的熟悉，那么此系列的方法将会使得您可以更加灵活的对数据列表进行操作。

在接口结构更新后，接口中所有的方法全部基于`get()`方法而不需要其他实现。

（虽然用户一般也不需要实现此接口。）

### v1.2 (2019.2.27)

※ 与上一版本不兼容点：将MockObject类变更为接口类型

支持直接获取Map类型，不再必须指定一个javaBean了

@函数中的字符串参数可以支持中文了，但是中文请务必放在单引号或双引号之中才可以生效

### v1.1  (2019.1.4)

支持导入自定义方法的导入

### v1.0  (2018.12.20)

更新README.md文档。



## 更新计划

(大概会咕很久)

* ~~支持生成Map键值对对象而非指定JavaBean对象~~( √ )
* ~~添加注解式的映射~~（√）
* ~~使@函数支持自定义~~( √ )
* ~~部分需要字符串的参数可支持中文~~( √ )

- 除了@函数以外，增加一个#参数，例如：如果你添加了一个map类型映射，取名“map1”,当你在其他映射中使用“#map1”的时候，边可直接添加映射名为map1的map映射。

  #参数 用于对映射进行嵌套，使得bean映射和map映射可以进行交互。目前两种映射还无法进行交互