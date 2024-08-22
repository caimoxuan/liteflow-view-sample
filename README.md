# Getting Started

## Dependencies

[liteflow-view]( https://github.com/caimoxuan/liteflow-view) 
will be renamed to springboot-starter;

springboot 3.x is different with springboot 2.x, so it has different version;
source code branch for springboot2.x is j_8_spring_2
source code branch for springboot3.x is j_17_spring_3

```xml
<dependency>
    <groupId>com.cmx</groupId>
    <artifactId>liteflow-view</artifactId>
    <version>${liteflow-view.version}</version>
</dependency>
```

## Config

it's a liteflow default value, but if you want to use the ui for liteflow, need to declare the properties;

```properties
liteflow.enable=true
```

## Run 

you need to run in springboot project, case it use the springboot autoConfigure; start your project and visit

ps: not support webflux!;

```http request
http://localhost:${server.port}/liteflow.html
```

## Extension Mode (Simple version)

扩展点的实现方式

1. java spi
   Java SPI，即 Java Service Provider Interface，是 Java 提供的一套供第三方实现或者扩展的 API，用于为某个接口寻找服务实现类，实现代码的解耦。
   spi的实现是由框架定义，使用者来实现，最后由框架调用， 所以实现的代码是需要集成到框架（把实现的jar引入或提前加载好实现），通用的有门面模式枚举所有扩展点让使用者实现后，打包引入；这样的实现方式比较稳定，代码可控，缺点是需要以集成的方式，扩展点的改动需要重新发版启动；
2. 即时脚本
   脚本的方式是说在扩展点执行的时候通过扩展点的业务编码和扩展点的代码找到当前扩展点的实现逻辑脚本（脚本可以在数据库，配置中心等远程保存），找到之后选择相应的脚本执行器来执行脚本返回扩展点的结果。
   这种方式虽然灵活， 发布脚本不用重新发版重启；但是一个挑战是脚本的编写成本和存储成本；编写成本说的是代码提示的方面（这点可以优化，能拿到java 类的字段类型，对代码编辑器做提示），编写相对困难一些；第二个是需要自己实现脚本的保存更新逻辑；

当前项目使用的是 ”2“ 这个方式；并且是简易版，支持如下：
1. 当前支持java 、 lua 2种语言；
2. 当前支持通过 bizCode(业务编码-对不同业务的区分) + extCode (扩展点编码-同项目唯一标识一个扩展点)来寻找扩展点；
3. 当前只支持单扩展点执行（不支持executeAll执行多扩展点，只能找到一个）；
4. 不支持多扩展点执行顺序定义；

一般来说比较复杂的项目才会使用到扩展点模式，并且一般配合DDD思想来进行领域拆分；而扩展点一般在 域能力实例 中，这里利用的就是liteflow中的组件作为域能力实例；

![扩展点使用](/doc/extension_sample.png)
（网上的图，但是哪来的找不到了...）

## Extension Sample

使用步骤也很简单，可以直接看 com.cmx.liteflow.liteflowviewsmaple.component.PostageCmp 中的实现

1. 定义一个扩展点
   参考 com.cmx.liteflow.liteflowviewsmaple.extension.GetPostAmountExt
   
   ```java
   @ExtensionPoint(extCode= GetPostAmountExt.EXT_CODE, extDesc = "获取运费金额")
   public class GetPostAmountExt extends AbstractExtensionNode<ExtensionData<String>, ExtensionParam> {
   
       public static final String EXT_CODE = "GET_POST_AMOUNT";
   
   }
   ```
   
   注意要继承 AbstractExtensionNode 泛型的第一个参数代表返回值 ExtensionData 类型， ExtensionData的泛型代表扩展点返回的类型， 可以自定义（有些类型没支持 比如 bigDecimal）；
   ExtensionData 中的 code 为 0 表示当前扩展点调用成功，返回其中的 data， 流程继续；如果返回的 code != 0 标识要终止当前流程，并会将其中的message当作异常抛出；
   第二个参数代表的扩展点的入参， 自定义入参可以继承 ExtensionParam；入参被 lua脚本解析为 userdata 例如想要获取其中的字段 需要使用 : 来调用（一般java类都使用getter setter， 字段是private的，字段申明为public可直接 .调用）
   ```lua
      local bizCode = param:getBizCode() 
   ```
   @ExtensionPoint 注解申明扩展点的 唯一code 和当前扩展点的说明，目前用来前端展示用；

2. 使用扩展点
   参考 com.cmx.liteflow.liteflowviewsmaple.component.PostageCmp
   ```
      Extensions.execute("00000100", GetPostAmountExt.EXT_CODE, new ExtensionParam(), GetPostAmountExt.class);
   ```
   第一个参数 00000100 代表的业务编码 实际业务中这个编码是上下文传入的，代表的不同的业务比如 天猫 淘宝是2个业务，有不同的标识；
   第二个参数 扩展点的code 一般申明在扩展点中， 直接使用不会出错；
   第三个参数 输入到扩展点中的参数， 可以自定义继承ExtensionParam， 赋值，在扩展点中能取到；
   第四个参数 扩展点的class 用来决定返回值和调用的实例

   ![使用完成后可重启查看当前流程是否有扩展点信息](/doc/extension_view.png)
   选择使用了扩展点的流程，使用了扩展点的组件会显示扩展点的数量，点击后右侧显示当前组件的扩展点信息；
   可点击扩展点进行查询、编辑；（这一步骤需要实现下面的步骤3）
   保存功能内置了对脚本的基础校验，防止语法错误的脚本提交（只校验是否能编译，默认参数运行，不校验返回值）

3. 实现扩展点的保存、更新、查询逻辑
   之前说过这个实现方式需要自己实现如何保存扩展点的脚本信息；
   可以参考 com.cmx.liteflow.liteflowviewsmaple.extension.loader.SelfRemoteExtensionLoader
   其中实现的是保存在本地文件中；实际使用的时候可能会保存在数据库、 配置中心等地方；需要自己实现
   
   ![扩展点操作](/doc/extension_op.png)
   bizCode是当前业务标识，通过业务标识查询扩展点脚本信息；
   选择脚本类型，编辑或新增脚本，保存后会调用自定义实现来保存或新增（当前查询到脚本会调用更新，否则调用新增）；
   
   注意点1： 扩展点存在缓存， 在更新和保存的时候需要调用 Extensions.clearExtensionCache(String bizCode, String extCode); 来清除缓存， 分布式环境下需要自己处理（异步消息通知等方式）；
   注意点2： 由于缓存的存在，不存在的扩展点需要返回 null 
   注意点3： 由于使用的spring， 需要将当前实现注入spring 且只能注入一个实现；
   注意点4： 由于使用的反射，扩展点声明的泛型一定要定义， 并且返回值定义不能是内部类；
   注意点5： 可能会注意到包体比较大， 是由于前端页面我将**前端编辑器**项目的使用文档全部都打包了其中存在一些视频图片，自己打包的化可以删除；
   
## Feature

一般复杂的应用， 简易模式已经够用了， 而且维护的复杂度也能接受；如果需要完整的扩展点功能，还需要实现的有很多；

1. 多脚本支持；
2. spi兼容；
3. 多扩展点实现支持；
4. 脚本代码提示；
5. 多扩展点顺序支持；
6. ...

## Refer

前端编辑器(https://gitee.com/imwangshijiang/liteflow-editor-client)

后端项目(https://gitee.com/dogsong99/liteflow-editor-server)

