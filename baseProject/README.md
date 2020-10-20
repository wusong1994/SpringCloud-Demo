# goumang
句芒基础开发框架

##目录命名规则
  - server-xxx 服务中心
  - out-xxx 对外提供服务，其中xxx取值
  - out-xxx-xxx 放置前端代码
  - unit-xxx  微服务
  - unit-xxx-api 微服务API
  - goumang-xxx 扩展包
  
##项目目录结构
  - document 文档中心
  - filters 公共配置、数据库变更脚本目录
  - goumang-core 核心库
  - goumang-oss 对象存储扩展包
  - out-console 对后端提供的服务
  - unit-pay 支付网关微服务
  - unit-sys 系统微服务
  - unit-sys-api 系统微服务api
  - pom.xml Maven项目构建文件
  
 ##搭建步骤
 1. 将各模块的pom.xml中报错的module，dependency删掉
 2. 执行document/db下的sql脚本，建立数据库(支持mysql5.7以上)
 3. 修改filters/application-development.properties的数据库、redis配置
 4. 将parent的pom.xml 的environment属性改为development

  - out-console-pc 后端pc端的代码
  - server-config 分布式配置中心
  - server-eureka 服务的注册发现中心
 ##项目启动
 1. 先启动服务注册发现中心，server-eureka，访问的地址为 http://localhost:8761
 2. 然后启动分布式配置中心，server-config，启动成功后在会注册到server-eureka，访问配置文件 http://localhost:8701/unit-sys/base
 3. 启动XXXApplication.java
 4. 在 out-console-pc目录下执行命令，npm install，安装js依赖包，安装完毕后执行命令 npm run serve启动，管理员用户名/密码：admin/123456
 