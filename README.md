## mmall-beta 学习日志

### maven 环境隔离，`dev`(开发环境) `beta`(测试环境) `prod`(生产环境) 之间的相互隔离和切换 

### redis安装（window/linux） 及其数据结构/基本命令 键命令

### 对`PropertiesUtil`进行了优化性的封装，提供获取`Integer` `Boolean` `String`等不同类型配置文件value的方法

### `redisPool`连接池的完善,获取jedis对象`getRedis()`，返回资源`returnResource()` 和 `returnBrokenResource()`使用，查看学习jedis源码

### IDE工具安装`lombok`工具，引入其依赖,使用注解，eg:@Sl4j @Setter @Getter @Data等，清楚各自含义

### `RedisPoolUtil`工具类封装，提供对Redis的set get del setEx expire等操作的方法

### 封装`JsonUtil`工具类，提供json和object之间相互转换方法，同时提供json转List<User> Map<User,Category>等复杂集合对象的转换方法

### `JsonUtil`objectMapper当中各种属性的配置 `
 + `objectMapper.setSerializationInclusion(Inclusion.ALWAYS);`
 + `objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);`
 + `objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);`
 + `objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));`
 + `objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);`
 
### `An invalid domain [.dianpoint.com] was specified for this cookie` 这个异常，使用cookie时候，Tomcat8和Tomcat7不太一样。
 + 解决方法：在Tomcat8的conf/content.xml当中添加如下配置
   `<CookieProcessor className="org.apache.tomcat.util.http.LegacyCookieProcessor" />`
   
   [An invalid domain was specified for this cookie](https://stackoverflow.com/questions/42524002/an-invalid-domain-was-specified-for-this-cookie)
   
   [How to change Cookie Processor to LegacyCookieProcessor in tomcat 8](https://stackoverflow.com/questions/38696081/how-to-change-cookie-processor-to-legacycookieprocessor-in-tomcat-8)