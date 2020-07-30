# Mock.java使用说明手册



## 简介

这是一个仿照Mock.js语法的Java语言使用的假数据生成工具框架。
部分方法与类介绍详细可查看JavaDoc文档(推荐先下载下来再看)：[JavaDoc文档](helpDoc/index.html)

码云生成的在线javaDoc文档：[在线文档](https://apidoc.gitee.com/ForteScarlet/Mock.java/)

如果存在BUG或者有什么意见、建议，可以通过邮箱`ForteScarlet@163.com`进行反馈或者联系QQ`1149159218`.
(记得注明身份哦~)

github: [github](https://github.com/ForteScarlet/Mock.java)

gitee : [码云地址](https://gitee.com/ForteScarlet/Mock.java)

此框架中不仅仅只可以作为假数据获取用，还有一些比较实用的工具类可以拿来单独使用。

*工具类介绍：<a href="#工具类介绍">工具类介绍</a>

当前版本：![[maven](https://search.maven.org/artifact/io.gitee.ForteScarlet/mock.java)](https://img.shields.io/maven-central/v/io.gitee.ForteScarlet/mock.java)

最低JDK版本：JDK8

※ 版本更新内容与预期更新计划详见于文档末尾 ： <a href="#更新公告">更新公告</a>

# **WIKI**

文档将会开始转移至WIKI处，转移完成后，此处README中的说明性文档将不再更新并择日删除，替换为简单的介绍与demo示例。

**wiki文档：[github wiki](https://github.com/ForteScarlet/Mock.java/wiki) or [gitee wiki](https://gitee.com/ForteScarlet/Mock.java/wikis/pages)**

## 注意
未来2.x版本将会使用与1.x版本不同的包路径。如果迭代版本请注意包路径的修改。
仅为修改包路径，其余内容不变。
如果有2.x的话

<br>

## 友情链接
|项目名称|项目介绍|项目地址|
|---|---|---|
|Mock.JDBC|基于Mock.java与JDBC向数据库插入假数据（暂时停工）|https://github.com/ForteScarlet/Mock.JDBC|


<br>

## 使用方法
### **Maven**

在maven项目下，从pom.xml中导入以下地址：
> 最新版本以maven仓库中的地址为准。仓库地址：`https://mvnrepository.com/artifact/io.gitee.ForteScarlet/mock.java`

```xml
<dependency>
    <groupId>io.gitee.ForteScarlet</groupId>
    <artifactId>mock.java</artifactId>
    <version>${version}</version>
</dependency>
```

### **Gradle**

```gradle
compile group: 'io.gitee.ForteScarlet', name: 'mock.java', version: '${version}'
```

### **Jar**

使用jar包导入的时候，记得同时把作为依赖的`commons-beanutils.commons-beanutils:1.9.3`中的jar包也导入进去。我上传了这些依赖在 [dependencies文件夹](./dependencies) 中。





### **使用**

相信使用过Mock.js的各位大佬应该知道，在使用Mock.js的时候是用的JSON格式的参数。
但是，Java可是没法直接识别JSON的啊！
所以，我们采用最接近JSON格式的方式：**Map集合**。

简单来说，就是将一个类的字段根据Mock.js那样的key-value的键值对转化为一个Map<String, Object>对象就好了！我习惯将这种Map对象称为 *字段映射表* 。

而且作为Java语言，数据类型是必须要多加考虑的问题。我在获取值的时候已经尽可能的增加了容错率，但是还是需要您注意数据类型的问题，请尽可能不要犯下将一个字符串赋值给整数这类难以防范的错误..

或许感觉上比JSON格式的使用要麻烦一些，但是这也是没有办法的事情嘛！假如您有更好的代替方式，希望您能告诉我 :) 


### 设置字段映射的方式：

#### 	1·创建对象字段与随机值语法的映射关系(Map<String , Object> 类型的键值对)

创建的这个Map，Key值代表了映射的字段名，value值代表了映射语法
由于这毕竟与弱引用类型语言不同，所以在设置映射的时候请务必注意字段的数据类型。

​	`Map<String, Object> map = new HashMap<>();`	

#### 	2·添加字段映射 

字段映射中，value值所用到的 @函数 可以从 [JavaDoc文档](https://apidoc.gitee.com/ForteScarlet/Mock.java/) 中查阅 [**MockUtil**](https://apidoc.gitee.com/ForteScarlet/Mock.java/com/forte/util/utils/MockUtil.html) 类中的方法，MockUtil中的全部方法均可作为 @函数 出现在value值中。

> **再次提醒，请务必注意对应好字段的字段类型**

```java
map.put("age","@age");
map.put("list|2-3","@title");
map.put("user","@name");  
......
```

key值中，有三种写法： 

仅有字段映射、字段映射与整数部分区间参数、字段映射、整数部分区间参数与小数部分区间参数。

例如如下这么两个字段映射：

```java
map.put("money1|10-40.2-4" , 0);
map.put("money2|10-40.2" , 0);
```

其中，字段名与区间参数之间的分割符为 **|** 符号，左边为字段名，右半边为区间参数。

区间参数中，整数部分与小数部分用 **.** 符号分割，左半边为整数部分区间参数，右半边为小数部分区间参数。

- ##### 仅有字段映射

   任务分配器首先会根据参数(value)的类型分配字段解析器，然后再根据字段类型进行取值。
  
    参数类型有一下几种情况：

    - **字符串类型**：如果存在一个或多个@函数，解析@函数并取值，（如果有多个@函数则会尝试对@函数的取值进行加法计算）；如果不存在@函数或@函数不存在于MockUtil中的方法列表则将其会视为普通字符串。
    - **整数（Integer）、浮点数（Double）类型**：如果参数为Integer或Double类型，则字段值获取器会直接将此值作为默认值赋给字段。
    - **数组或集合**：如果参数是数组或集合类型，字段值获取器会从其中随机获取一个值赋予字段。
    - **Map集合**：如果参数是Map集合类型，则会对字段的类型进行判断，如果：
      - 字段为Map类型，则直接将此Map作为字段值赋予字段，不做处理。
      - 字段为List<Map>类型，则字段值获取器会将将此Map集合封装至List集合中并返回。
      - 字段为其他任意类型，则任务分配器会将此Map视为此字段类型的字段映射集合进行解析并获取一个实例对象为字段赋值。**(※注：此字段映射同样会被Mock的映射集合记录下来。即嵌套的字段映射不需要单独再进行set了。)**
    - **其他任意类型**：如果参数不是上面的任何类型，则字段值获取器会将此参数原样赋值，不做处理。

   ```java
   //假设以下字段映射的是User类
   Map<String, Object> map = new HashMap<>();
   map.put("age1" , "@age");
   map.put("age2" , 15);
  map.put("age3" , new Integer(){1,2,3,4});
   map.put("name1" , "@name");
   map.put("name2" , "@title(2)");
   map.put("name3" , "这是一个名字");
   
   //下面三个email字段的参数，如果是中文，必须放在单引号或双引号中才会生效，英文不受限制
   map.put("email1" , "@email('这是中文')");
   map.put("email2" , "@email('this is english')");
   map.put("email3" , "@email(this is english)");
   
   //下面的friend字段的字段类型是一个Friend类，friendMap是对friend字段的映射，也就是嵌套映射
   //此friendMap的映射无需单独进行记录
   map.put("friend" , friendMap);
   
   //记录映射
   Mock.set(User.class, map);
   //User类的映射被直接记录，可以获取
   MockObject<User> userMockObject = Mock.get(User.class);
   //Friend类的映射以嵌套的形式被记录过了，可以直接获取
   MockObject<Friend> friendMockObject = Mock.get(Friend.class);
  ```

 - ##### 字段映射与仅整数部分的区间参数

    参数类型有一下几种情况：
   >
    - **字符串类型**：如果存在一个或多个@函数，解析@函数并取值，（如果有多个@函数则会尝试对@函数的取值进行加法计算）；在存在@函数的情况下，区间参数将被忽略。
   
      **※ 从`v1.4.2与v1.4.3`之后，当字段类型为Object类型（常见于创建Map类型对象）则会根据区间函数创建范围内大小的List集合。（详细见`v1.4.2与v1.4.3`更新日志）**
  
   
   
      如果不存在@函数或@函数不存在于MockUtil中的方法列表则将其会视为普通字符串，然后根据整数参数区间获取一个随机数值并对此字符串进行重复输出。
   
    - **整数（Integer）、浮点数（Double）类型**：如果参数为Integer或Double类型，则字段值获取器将区间参数作为方法参数，根据字段的类型使用随机函数获取对应的随机值。
   
      例如：
   
      ```java
      //age为一个Integer类型的字段，等同于使用了@integer(2,4)函数
      map.put("age|2-4" , 0);
      //money为一个Double类型，等同于使用了@doubles(2,4)函数
      map.put("money|2-4" , 0);
      ```
   
      则age将会被赋予一个2-4之间的随机整数(Integer)，money将会被赋予一个2-4之间最大小数位数为0的浮点数(Double)。
   
    - **数组或集合**：如果参数是数组或集合类型，任务分配器会判断字段的类型分配字段值获取器：
   
      - 字段类型为整数或浮点数，则区间参数将会被忽略，直接从参数中获取一个随机元素并赋值。
   
        *如下所示三种情况的取值是完全相同的：*
   
        ```java
        //age为一个Integer类型的字段
        map.put("age|2-4" , new Integer[]{1,2,3});
        map.put("age|2" , new Integer[]{1,2,3});
        map.put("age" , new Integer[]{1,2,3});
        ```
   
      - 字段类型为数组或集合的时候，会根据区间参数获取一个随机数量，并从传入的参数中获取此数量的随机元素。
   
    - **Map集合**：如果参数是Map集合类型，则任务分配器会对字段的类型进行判断，如果：
   
      - 字段为Map类型，则直接将此Map作为字段值赋予字段且忽略区间参数，不做处理。
   
      - 字段为List<Map>类型，则字段值获取器会将将此Map集合封装至List集合并根据区间参数重复一个随即数量并返回。
   
      - 字段为List<? extends Object> 类型，即一个任意泛型的List类型的时候，任务分配器会将此Map视为此泛型类型的字段映射集合进行解析，再根据区间参数获取指定范围内数量的实例对象，并封装为List类型为字段赋值。**(※注：上文提到过，内嵌字段映射同样会被记录。)**
   
      - 字段为其他任意类型，则任务分配器会将此Map视为此字段类型的字段映射集合进行解析并获取一个实例对象为字段赋值，忽略区间参数。**(※注：上文提到过，内嵌字段映射同样会被记录。)**
   
   
   
        ※ 在`v1.4.3`版本之后，存在整数区间的Map映射机制存在改动（主要为结果对象为Map类型的时候）详见`v1.4.3`版本日志。
   
    - **其他任意类型**：
   - 如果字段是list类型或数组类型，则会根区间参数重复输出并为字段赋值。
      - 如果字段类型为其他未知类型，则会忽略区间参数并使用参数值作为默认值赋值。
   
```java
   map.put("list|2-6" , "@title");
   map.put("age|10-40" , 2);
```

 - 字段映射、整数区间参数和小数区间参数

    参数类型有一下几种情况：
   
   - **字符串类型**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
   
    - **整数（Integer）、浮点数（Double）类型**：如果字段类型为整数，则会无视小数部分区间参数，与整数区间参数时的情况相同（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
  
     如果字段类型为小数，则会根据区间参数获取一个指定区间内的随机小数，例如：
   
      ```java
      // money为一个Double类型的字段，此映射等同于使用了@doubles(2,4,2,4)函数
      map.put("money|2-4.2-4" , 0);
      // money为一个Double类型的字段，此映射等同于使用了@doubles(2,4,2)函数
      map.put("money|2-4.2" , 0);
      ```
   
    - **数组或集合**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
   
    - **Map集合**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。
   
    - **其他任意类型**：同仅整数区间参数时的情况。（ <a href="#字段映射与整数部分的区间参数">字段映射与仅整数部分的区间参数</a> ）。




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
// 获取一个结果，并使用Optional类进行封装。
Optional<T> get();
```

```java   
// 获取一个结果
T getOne();
```

```java
// 获取指定数量的多个结果，返回List集合
List<T> getList(int num);
```


```java
// 获取指定数量的多个结果，并根据给定规则进行转化，返回List集合
List<R> getList(int num , Function<T, R> mapper);
```

```java
// 获取指定数量的多个结果，返回Set集合
Set<T> getSet(int num);
```
```java
// 获取指定数量的多个结果，并根据给定规则进行转化，返回Set集合
Set<R> getSet(int num , Function<T, R> mapper);
```


    ```java
// 获取指定数量的多个结果，并根据给定规则转化为Map集合
Map<K,V> getMap(int num , Function<T,K> keyMapper, Function<T,V> valueMapper);
    ```




​    
​    **※ 自1.3版本之后，我优化了`MockObject`接口内部结构，并增加了大量parallel方法与collect方法，您现在可以在1.3版本中更加灵活的对数据进行转化，或者根据数据量的需求自行决定是否需要使用并行线程进行对象创建。**

## **自定义@函数**

有时候，我提供的MockUtil中的方法可能无法满足您的需求，那么这时候，就需要一个可以对@函数进行扩展、加强的窗口。在v1.1版本中，我添加了这个功能。(这个功能测数量很少，可能会存在很多bug)

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



# **注解形式映射**

1.4版本之后我提供了两个可以使用在字段上的注解：`@MockValue` 和 `@MockArray`

## @MockValue

使用在类的字段上，参数：

```java
    /**
     * 映射值，如果为空则视为无效
     */
    String value();

	/* --- 1.6.0后增加 --- */

    /**
     * 区间参数，如果有值，则代表了字段之前的区间参数。默认没有值
     * 例如当字段{@code age} 的注解参数为 {@code param = "10-20"} 的时候, 相当于字段值为 {@code "age|10-20"}。参数中的那个竖线不需要写。写了也会被去除的。
     * @since  1.6.0
     */
    String param() default "";

    /**
     * 参数value的最终类型，在转化的时候会使用beanutils中的工具类
     {@link org.apache.commons.beanutils.ConvertUtils}进行类型转化, 默认为String类型。
     * @return
     */
    Class<?> valueType() default String.class;
```

也就是说，假设这个字段叫做：`field_A`，则映射结果大致相当于：

```java
// 其中，${value()} 的最终结果值为通过ConvertUtils进行转化的结果。
// 其中，[|${param()}]的存在与否取决于param()里有没有值
xxxMap.put("${field_A}[|${param()}]", (${valueType()}) ${value()})
```



用来指定此字段的映射值。例如：

```java
public class User {
    
    // 相当于 ("name", "@cname")
    @MockValue("@name")
    private String name;
    
    // 相当于 ("age|20-40", 0)
    @MockValue(value = "0", param = "20-40", valueType = Integer.class)
    private Integer age;

   // 省略 getter & setter
    
}
```





## @MockArray

使用在类的字段上，参数：

```java
	/**
     * 数组参数, 必填参数
     */
    String[] value();

    /**
     * 类型转化器实现类，需要存在无参构造
     * 默认转化为字符串，即默认不变
     */
    Class<? extends ArrayMapper> mapper() default ArrayMapperType.ToString.class;

	/* --- 1.6.0后增加 --- */

    /**
     * 区间参数，如果有值，则代表了字段之前的区间参数。默认没有值
     * 例如当字段{@code age} 的注解参数为 {@code param = "10-20"} 的时候, 相当于字段值为
     {@code "age|10-20"}。参数中的那个竖线不需要写。写了也会被去除的。
     * @since  1.6.0
     */
    String param() default "";
```

其中，`mapper()`参数可选，其类型为`ArrayMapper`接口的的实现类，用于指定将字符串数组，也就是`value()`中的值进行转化的规则。此参数默认为不进行转化，即转化为字符串类型。

`ArrayMapper`接口中的抽象方法：

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

   // 省略 getter & setter
}
```





## **使用**

使用也很简单，我在`Mock`中增加了4个方法，2个`set`方法 2个`reset`方法。

```java
	/* --- 1.4版本之后增加 --- */	

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



## **注意事项**

### 注解优先级

假如你在同一个字段上同时使用了两个注解，则会优先使用`@MockValue`;


### 额外映射

可以发现，4个方法中各有一个方法需要提供额外参数，他会在注解映射创建完毕后进行添加，也就是假如额外参数和字段中有冲突的键，则额外参数的值将会覆盖注解映射值。


# **映射扫描**

1.6.0版本后，我更新了**映射扫描**与**映射代理**功能。感谢提出建议的朋友。[Issue#I1CCMT](https://gitee.com/ForteScarlet/Mock.java/issues/I1CCMT)

在您使用注解形式映射的时候，是否有感觉到每个类都需要使用`Mock.set(...)`进行设置很麻烦？希望能够通过包扫描一键批量set？现在我增加了一个注解：`@MockBean`，将其标注在您的类上，此时再配合使用`Mock.scan(...)`方法即可扫描指定的一个或多个包路径中所有标注了`@MockBean`的javaBean。

对于`Mock.scan(...)`的方法定义如下：

```java
    /**
     * 扫描包路径，加载标记了{@link com.forte.util.mapper.MockBean}注解的类。
     *
     * @param classLoader nullable, 类加载器, null则默认为当前类加载器
     * @param withOther   nullable, 假如扫描的类中存在某些类，你想要为它提供一些额外的参数，此函数用于获取对应class所需要添加的额外参数。可以为null
     * @param reset       加载注解映射的时候是否使用reset
     * @param packages    emptyable, 要扫描的包路径列表, 为空则直接返回空set
     * @return 扫描并加载成功的类
     */
    public static Set<Class<?>> scan(ClassLoader classLoader, Function<Class<?>, Map<String, Object>> withOther, boolean reset, String... packages) throws Exception;

```

这么多参数？先别怕，我先简单介绍下这些参数：

- classLoader：包扫描使用的类加载器。**可以为null。**
- withOther：一个Function函数，这个参数接收一个`Class`参数，返回一个`Map<String, Object>`结果，即获取一个对应类的额外参数。类似于注解映射中set方法的额外映射。**可以为null。**
- reset：即如果扫描到了已经被添加的映射，是否覆盖。
- packages：需要扫描的包路径列表。

除了这个方法，我还提供了一些重载方法：

```java
    /**
     * {@link #scan(ClassLoader, Function, boolean, String...)}的重载方法
     * @see #scan(ClassLoader, Function, boolean, String...)
     */
    public static Set<Class<?>> scan(Function<Class<?>, Map<String, Object>> withOther, boolean reset, String... packages) throws Exception;

    /**
     * {@link #scan(ClassLoader, Function, boolean, String...)}的重载方法
     * @see #scan(ClassLoader, Function, boolean, String...)
     */
    public static Set<Class<?>> scan(boolean reset, String... packages) throws Exception;

    /**
     * {@link #scan(ClassLoader, Function, boolean, String...)}的重载方法, reset默认为false
     * @see #scan(ClassLoader, Function, boolean, String...)
     */
    public static Set<Class<?>> scan(String... packages) throws Exception;
```



## **使用**

所以一般情况下，你可以直接这么使用：

```java
// 扫描两个包
Mock.scan("forte.test2.beans", "forte.test1.beans", ...);
// 然后直接获取
Mock.get(Xxxx.class);
// 使用 
```

# **映射代理**

1.6.0版本后，我更新了**映射扫描**与**映射代理**功能。感谢提出建议的朋友。[Issue#I1CCMT](https://gitee.com/ForteScarlet/Mock.java/issues/I1CCMT)

首先看一下Issue上提出的模拟场景：

```java
// interface 
public interface ServiceA{
    VoA methodA();
}
// bean, can with @MockBean
public class VoA{
    @MockValue("@cname")
    private String p1;
}
```



此时，接口中的`methodA()`方法的返回值`VoA`恰好是一个MockBean，这时候，我想要得到`ServiceA`的一个代理对象，使其能够通过`methodA()`得到`VoA`的实例对象。

ok，因此我添加了方法`Mock.proxy(...)`及其重载。方法定义如下：

```java
    /**
     * <pre> 为一个接口提供一个代理对象。此接口中，所有的 抽象方法 都会被扫描，假如他的返回值存在与Mock中，则为其创建代理。
     * <pre> 此方法默认不会为使用者保存单例，每次代理都会代理一个新的对象，因此如果有需要，请保存一个单例对象而不是频繁代理。
     * @param type    要代理的接口类型。
     * @param factory 接口代理处理器的获取工厂。可自行实现。
     * @param <T> 接口类型
     * @return 代理结果
     */
    public static <T> T proxy(Class<T> type, MockProxyHandlerFactory factory);
```

此方法传入一个接口类型`Class<T> type` 和一个动态代理处理器`MockProxyHandlerFactory factory`，来获取一个代理对象。

`MockProxyHandlerFactory`是一个接口类型，只存在一个抽象方法：

```java
    /**
     * 获取代理处理接口{@link InvocationHandler}实例
     * @param mockObjectFunction 传入一个类型和一个可能为null的name字符串，获取一个mockObject对象。如果存在name，则会尝试先用name获取
     * @return JDK动态代理所需要的代理处理器示例。
     * @see InvocationHandler
     */
    InvocationHandler getHandler(BiFunction<Class<?>, String, MockObject<?>> mockObjectFunction);

```

此接口定义了如何创建一个代理类。`InvocationHandler`是JDK为动态代理的创建所提供的一个接口，知道动态代理的人对他应该不会很陌生。

但是如果不熟悉也没关系，我在内部提供了一个默认的实现`MockProxyHandlerFactoryImpl`，同时也为`Mock.proxy(...)`提供了一个重载方法：

```java
    /**
     * <pre> 为一个接口提供一个代理对象。此接口中，所有的 抽象方法 都会被扫描，假如他的返回值存在与Mock中，则为其创建代理。
     * <pre> 此方法默认不会为使用者保存单例，每次代理都会代理一个新的对象，因此如果有需要，请保存一个单例对象而不是频繁代理。
     * <pre> 使用默认的接口代理处理器工厂{@link MockProxyHandlerFactoryImpl}。
     * <pre> 默认处理工厂中，代理接口时，被代理的方法需要：
     * <pre> 不是default方法。default方法会根据其自己的逻辑执行。
     * <pre> 没有参数
     * <pre> 没有标注{@code @MockProxy(ignore=true) ignore=true的时候代表忽略}
     * <pre>
     * @see MockProxyHandlerFactoryImpl
     * @param type    要代理的接口类型。
     * @return 接口代理
     */
    public static <T> T proxy(Class<T> type);
```

因此，一般情况下，你可以直接这么使用：

```java
MockTestInterface proxy = Mock.proxy(MockInterface.class);
```

让我们来创建一个示例接口来看看：

```java
public interface MockTestInterface {

    /**
     * 指定List的泛型类型为User类型，长度为2-4之间
     */
    @MockProxy(size = {2, 4}, genericType = User.class)
    List<User> list();

    /**
     * 默认情况下，长度为1
     */
    Teacher[] array();

    /**
     * 直接获取，不用注解。
     */
    Admin admin();
    
    /**
     * 获取name为{@code mock_map}的mockObject, 基本上返回值都是Map类型。
     */
    @MockProxy(name = "mock_map")
    Map<String, String> map();

    /**
     * 忽略, 返回值会默认为null
     * @return null
     */
    @MockProxy(ignore = true)
    Admin justNullAdmin();
}
```



可以看到，上面的这些抽象方法中，有一部分方法标注了注解`@MockProxy`。此注解参数如下：

```java

    /**
     * <pre> 是否忽略此方法。如果为是，则接口的最终代理结果为返回一个null。
     * <pre> 当然，如果获取不到对应的Mock类型，无论是否忽略都会返回null或者默认值。
     * <pre> 如果是基础数据类型相关，数字类型，返回{@code 0 或 0.0}。
     * <pre> 如果是基础数据类型相关，char类型，返回{@code ' '}。
     * <pre> 如果是基础数据类型相关，boolean类型，返回{@code false}。
     */
    boolean ignore() default false;

    /**
     * 如果此参数存在值，则会优先尝试通过name获取MockObject对象。一般应用在返回值为Map类型的时候。
     */
    String name() default "";

    /**
     * <pre> 当接口返回值为数组或者集合的时候，此方法标记其返回值数量大小区间{@link [min, max], 即 max >= size >= min}。是数学上的闭区间。
     * <pre> 如果此参数长度为0，则返回值为1。
     * <pre> 如果参数长度为1，则相当于不是随机长度。
     * <pre> 如果参数长度大于2，只取前两位。
     */
    int[] size() default {1,1};

    /**
     * <pre> 指定返回值类型，三种可能类型：list类型，array类型，Object其他任意类型。默认值为Unknown类型。当为Unknown类型的时候，会根据返回值类型自动判断。
     * <pre> 当类型为list与array类型的时候，需要通过{@link #genericType()}方法指定泛型的类型，获取mock类型的时候将会通过此方法得到的类型来获取。
     */
    MockProxyType proxyType() default MockProxyType.UNKNOWN;


    /**
     * 假如类型为List或者数组类型，此处代表泛型的实际类型。
     */
    Class<?> genericType() default Object.class;


```



简单汇总一下此注解的参数：

- ignore：忽略这个方法。

- name：指定mockObject的name。一般在返回值为Map类型的时候使用。

- size：指定大小区间。只有在返回值为array或者List类型的时候才有用。

- proxyType：指定返回值类型。一般情况下可以自动推断。

- genericType：当返回值为List类型的时候，此参数指定他的泛型类型。array类型可以进行推断。

  

`@MockProxy`并不是必须的，但也不是可能完全省略的，需要根据实际情况来。一般来讲，如果返回值是一个任意的bean类型，则基本上可以省略，而数组类型如果没有长度要求的话也可以省略，但是例如list、map类型基本上是需要配置部分参数的。



## **使用**

接着上面的`MockTestInterface`，在使用的时候就是这个样子的：

```java
public static void main(String[] args) throws Exception {
        // 扫描包
        Mock.scan("com.test");
        // 注册一个map类型的mockbean
        Mock.set("mock_map", new HashMap<String, Object>(){{
            put("name_map", "@cname");
        }});

        // 获取接口代理
        final MockTestInterface proxy = Mock.proxy(MockTestInterface.class);

        // 输出测试
        System.out.println("proxy.admin(): " + proxy.admin());
        System.out.println("proxy.justNullAdmin(): " + proxy.justNullAdmin());
        System.out.println("proxy.list(): " + proxy.list());
        System.out.println("proxy.array(): " + Arrays.toString(proxy.array()));
        System.out.println("proxy.map(): " + proxy.map());

}
```

# #函数
(v1.7.0更新，参阅wiki `8_#function&class parse`)



# List区间(v1.7.0)
(v1.7.0更新，参阅wiki `9_List interval parameter(v1.7.0)`)



## **注意事项**

- 尽可能别用泛型。

- 每次使用`Mock.proxy(...)`都会去生成动态代理对象，会影响性能，所以尽可能保存成一个单例使用。





# **使用依赖列表**
```xml
<dependency>
   <groupId>commons-beanutils</groupId>
   <artifactId>commons-beanutils</artifactId>
   <version>1.9.3</version>
</dependency>
```


## 更新公告

### **v1.9.1(2020/07/30)**
修复`Mock.set(...)`使用Map类型的时候会报空指针的问题


### **v1.9.0(2020/07/30)**
优化实例的获取效率（尤其是以获取中文内容为主的时候）
效率提升约3~4倍左右。
测试参考：100w条实例获取，getList(100w)约16s, getListParallel(100w)约7s


### **v1.8.0(2020/07/13)**
增加对某个类的父类字段的处理 
fix [#I1NLT4](https://gitee.com/ForteScarlet/Mock.java/issues/I1NLT4)

优化mock值的获取效率（大概提升了30倍），但是当值为字符串的时候，默认情况下不会再尝试将其视为JS脚本执行了。
如果想要开启JS脚本尝试功能，请使用静态配置类`MockConfiguration.setEnableJsScriptEngine(true)`
fix [#I1NLWA](https://gitee.com/ForteScarlet/Mock.java/issues/I1NLWA)


`Mock`中增加三个`setAndGet(...)`方法，故名思意，整合先set后get的流程。


### **v1.7.1(2020/04/11)**
修复由于我的疏忽，导致在使用注解创建映射而映射类中存在没有注解的字段的时候，会出现空指针异常的问题。
fix gitee issue [#I1E46D](https://gitee.com/ForteScarlet/Mock.java/issues/I1E46D)


### **v1.7.0(2020/04/01)**
- 增加一个“#函数”, 其映射已经添加进Mock中的映射名称。例如: 
```java
        Map<String, Object> map = new HashMap<>();
        map.put("name", "@name");
        // set 'name_map'
        Mock.set("name_map", map);

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("mapList", "#name_map");

        //set 'user_map'
        Mock.set("user_map", userMap);
        MockObject<Map> userMapMock = Mock.get("user_map");

        // show 
        System.out.println(userMapMock.getOne());
```
- 增加对于`Map`中，value类型为`Class`类型的参数的解析。类似于上述的“#函数”，只不过参数不是`'#xxx'`而是一个指定的`Class`对象。
例如：
```java
        Map<String, Object> map = new HashMap<>();
        map.put("name", "@name");
        map.put("user", User.class);           // will get User from Mock
        map.put("userList|1-2.0", User.class); // will get User list from Mock
```
- 增加针对于`List`类型参数的规则修改。
以前版本，假如一个字段叫做`users`, 是一个List类型，此时，我填入的参数为：
```java
map.put("users|1-2.3-4", otherMap);
```
这个时候，区间参数中的`3-4`将会被忽略不计。
当前版本，由于加入了`Class解析`与`#函数`，使得各种不同类之间的嵌套成为可能（甚至是自己嵌套自己）
此时，我修改了`List`类型的字段的区间参数规定，以上述例子为例，现在`3-4`并不会被忽略了，List结果的最终输出长度，会从`1-2`这个区间和`3-4`这个区间中随机选择一个。
也就是说，假如你写了：
```java
map.put("users|1000.0", User.class);
```
那么最终的结果里，`users`字段的长度，要么是1000，要么是0。
而如果你写了：
```java
map.put("users|5-10.200-300", User.class);
```
那么最终的结果里，`users`字段的长度，要么在5-10之间，要么在200-300之间。


### **v1.6.0(2020/3/25)**

**增加功能：映射扫描、映射代理**

删除某些无用代码
删除文档开始的一些废话
简单改善部分代码
增加一个注解`@MockBean`, 使用在类上，当使用包扫描功能的时候，只会扫描被标记了此注解的类。配合两个注解映射使用。
注解`@MockValue`增加参数：`String param() default ""`、`Class<?> valueType() default String.class`
注解`@MockArray`增加参数：`String param() default ""`
`Mock`中增加了一个方法`scan(Function<Class<?>, Map<String, Object>> withOther, boolean reset, String... packages)`以及部分重载方法，用来根据`@MockBean`注解来进行扫描与批量的注解映射注册。返回值为加载后的class列表

增加一个注解`@MockProxy` 标记接口代理中，一些需要特殊处理的抽象方法，例如返回值为Map或者忽略参数等。
`Mock`中增加了一个方法

着手准备编写wiki
删除helpDoc文件夹

### **v1.5.2(2020/2/22)**
修复在使用ChineseUtil的时候会在控制台打印所有的姓氏的问题


### **v1.5.1(2019.12.13)**
修复自定义函数添加无效的bug


### **v1.5.0(2019.12.5)**
变更MockMapObject的获取值类型为Map（原本是Map<String, Object>）
本质上依旧获取的是Map<String, Object> 类型。
内部增加一些参数以适应扩展开发。


### **v1.4.4(2019.12.4)**
优化内部Random操作，现在理论上Random相关操作的效率会高一些了。


### **v1.4.3(2019.10.18)**

在上一个版本的基础上又为Map类型的参数与Object类型参数增加了List值返回。
现在可以更好的支持例如fastJson等Json工具了。
（测试量较少，可能存在些许bug）




### **v1.4.2(2019.10.18)**
修复机制：当生成Map类型的值的时候，假如字段映射类似于这种格式：
```json
{
    "a": {
        "array|5-10":"@name"
    }
}
```
更新之前，`array`字段将会忽略区间参数`5-10`直接根据`@name`生成随机姓名，更新后则将会生成长度在5-10之间的list集合。
引申思考：如果参数不是字符串而是数字，如何判断是获取数组还是获取区间内的随机数字？
于是在更新计划中追加一项以在特殊情况下指定生成的字段的数据类型


修复当参数为List类型的时候，作为参数List没有被copy的bug。

距离功能写完到现在，我意识到了曾经的自己十分喜欢使用异步并行流遍历，然而这不一定是效率最高的，
所以在内部我修改了两处我看到的并行流遍历代码并修改为了单线程。




### **v1.4(2019.8.26)**

提供两个注解以实现注解形式的映射。(测试量较少不知道有没有bug)



### **v1.3(2019.7.12)**

优化`MockObject`接口内部接口，增加大量`parallel`(并行流)方法与`collect`方法。

`parallel`相关方法将会在您创建对象的时候使用并行线程进行创建，当您需求的数据量较大的时候，此系列方法将会相对于原本的串行方法更加有效率。

`collect`相关方法将会提供大量参照于`Stream`中的`collect`方法而制定的方法。假如您对于`Stream`中的`collect`方法十分的熟悉，那么此系列的方法将会使得您可以更加灵活的对数据列表进行操作。

在接口结构更新后，接口中所有的方法全部基于`get()`方法而不需要其他实现。

（虽然用户一般也不需要实现此接口。）

### **v1.2 (2019.2.27)**

※ 与上一版本不兼容点：将MockObject类变更为接口类型

支持直接获取Map类型，不再必须指定一个javaBean了

@函数中的字符串参数可以支持中文了，但是中文请务必放在单引号或双引号之中才可以生效

### **v1.1  (2019.1.4)**

支持自定义方法的导入

### **v1.0  (2018.12.20)**

更新README.md文档。

