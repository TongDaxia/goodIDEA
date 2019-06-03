##### api模块：

公共接口、pojo

##### Consumer-80

客户端负载均衡工具Ribbon

Ribbon的导入：config、RandomRule

注意，如果是自定义负载均衡算法，类的位置如果放在引导类外面会导致  `RestTemplate` 报找不到错误。

##### Consumer-Feign-8088

利用**Feign**框架做负载均衡。

Feign 是基于Ribbon负载均衡的实现，Feign对Robbon进行了封装。

controller("/feign/dept/list")

##### Eureka-7001

- Netflix公司基于REST的服务，用于定位服务，以实现云端中间层服务发现和故障转移。
- Eureka主管服务注册与发现，在微服务中，以后了这两者，只需要使用服务的标识符（就是那个在每个服务的yml文件中取得服务名称），就可以访问到服务，不需要修改服务调用的配置文件
- Eureka遵循AP原则（高可用，分区容错性），因为使用了自我保护机制所以保证了高可用

> Eureka的自我保护机制主要是为了网络异常时保持高可用设计的，当在Eureka中注册的微服务超过设定是时间内（默认90秒）没有向Eureka服务端发送心跳，该微服务会进入自我保护模式。在自我保护模式中，Eureka会保护服务注册表中的信息，不会注销任何服务实例，直至收到的心跳数恢复至阈值以上，该微服务退出自我保护模式。
>
> 一般推荐**打开**自我保护服务。

##### Eureka-7002

##### Eureka-7003

##### DashBoardApplication

小刺猬是一个监控工具，可以统计provider的运行情况，并可视化体现在页面上。

需要配合hystrix 才好使用，普通的提供者并不能有效的连接显示。

##### Provider-8001

controller  /dept/list

service

mapper

##### Provider-8002

controller  /dept/list

service

mapper

##### Provider-8003

controller  /dept/list

service

mapper



##### Provider-hystrix-8004

熔断器

由于服务间相互依赖，某一个服务停用会使得不可用范围扩大。

服务雪崩的解决方案：

超时机制：为每个请求设置超时时间；

断路器模式：实现快速失败

Hystrix主要的作用就是：服务的熔断、服务降级、服务限流、近实时监控。

注意，如果对应的方法出现了错误，执行了熔断方法，后续的其他请求将不会被执行，直接PASS了！

##### Zuul-8888

filter 网关。









查看服务注册情况：

http://eureka03:7001

http://eureka03:7002

http://eureka03:7003

服务提供者：

http://localhost:8001/dept/list

http://localhost:8002/dept/list

http://localhost:8003/dept/list

http://localhost:8004/dept/list

provider中：

http://localhost:8002/provider/discovery

consumer中：

http://localhost/consumer/discovery

负载均衡Ribbon，轮询：

http://localhost/consumer/dept/list





监视&可视化：

http://localhost:9999/hystrix

http://localhost:8004/hystrix.stream



