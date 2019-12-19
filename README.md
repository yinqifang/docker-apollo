使用Docker部署Ctrip Apollo
===
# 1. 说明
Apollo（阿波罗）是携程框架部门研发的开源配置管理中心，这里记录一下自己使用Docker-compose搭建的一套分布式部署环境。
## 1.1 官方文档
* [Apollo配置中心介绍](https://github.com/ctripcorp/apollo/wiki/Apollo%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83%E4%BB%8B%E7%BB%8D)
* [分布式部署指南](https://github.com/ctripcorp/apollo/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2%E6%8C%87%E5%8D%97)
* [Java客户端使用指南](https://github.com/ctripcorp/apollo/wiki/Java%E5%AE%A2%E6%88%B7%E7%AB%AF%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97)
# 2. 准备环境
## 2.1 基础环境
* CentOS 7
* Docker 18.09.7
* docker-compose 1.24.0
* Java 8
* MySQL 5.7
* Apollo v1.5.1
## 2.2 项目结构
一共有8个Docker容器，搭建出生产（pro）和测试（dev）两个环境。测试客户端（未在项目图中列出）使用本地运行方式，方便调试。
* Mysql：
    * mysql-apollo-config-dev： 测试环境config数据库
    * mysql-apollo-config-prod： 生产环境cofnig数据库
    * mysql-apollo-portal-prod： 生产环境portal数据库
* 服务：
    * config service
        * apollo-configservice-prd：生产环境config service
        * apollo-configservice-dev：测试环境config service
    * admin service
        * apollo-adminservice-prd：生产环境admin service
        * apollo-adminservice-dev：测试环境admin service
    * portal
        * apollo-portal-prd：生产环境portal
![](https://github.com/yinqifang/docker-apollo/blob/master/ApolloWithDockerStructure.png)
# 3. 下载源码
个人推荐采用下载源码的方式构建，遇到问题还可以翻翻源码查原因。<br>
地址：https://github.com/ctripcorp/apollo <br>
下载master分支，然后切换到1.5.1的tag
# 4. 创建数据库
使用官方5.7版本镜像，导入数据库初始化脚本（源码中获取）。<br>详细配置参考本工程mysql模块和docker-compose-mysql.yml
源脚本位置：
* configdb：
    apollo/scripts/db/migration/configdb/V1.0.0__initialization.sql
* portaldb：
    apollo/scripts/db/migration/portaldb/V1.0.0__initialization.sql

初始化后的数据库需要修改部分配置（eureka地址、支持的环境等），已经放到对应目录的z_update.sql脚本中了，docker启动时会自动执行。
# 5. 打包
源码中编译config service、admin service和portal的zip包。由于zip包较大，项目中没有上传，需要自行打包后拷贝到相应目录
## 5.1 修改配置
官方文档中需要修改scripts/build.sh中的配置，此处由于数据库、meta service的路径都是通过外置文件导入到docker容器中的，所以不需要修改任何配置，直接build
## 5.2 编译
命令行定位到apollo/scripts路径下，执行./build.sh
~~~
./build.sh
~~~
## 5.2 config service
将 {Apollo源码目录}/apollo-configservice/target/apollo-configservice-1.5.1-github.zip 复制到{本项目目录}/apollo/configservice 目录下
## 5.3 admin service
将 {Apollo源码目录}/apollo-adminservice/target/apollo-adminservice-1.5.1-github.zip 复制到{本项目目录}/apollo/adminservice 目录下
## 5.4 portal
将 {Apollo源码目录}/apollo-portal/target/apollo-portal-1.5.1-github.zip 复制到{本项目目录}/apollo/portal 目录下
# 6. 创建服务
服务端配置参考apollo路径下docker配置和docker-compose-apollo.yml文件，数据库信息可以按需修改
# 7. 启动
## 7.1 启动数据库
~~~
cd scripts
bash mysql-start.sh
~~~
## 7.2 启动服务
~~~
cd scripts
bash apollo-start.sh
~~~
# 8. 验证服务
服务启动有点慢，耐心等待
## 查看Eureka
* http://apollo-configservice-dev:8080/
* http://apollo-configservice-prd:8080/

Eureka中要能看到config service和admin service两个服务才正常
## 登录portal
* http://apollo-portal-prd:8070/
    ~~~
    用户名：apollo
    密码：admin
    ~~~
# 9. 配置properties
至此服务端已经全部配置完毕，可以开始使用啦！<br>
登录进portal，创建对应的配置，测试客户端中使用的配置：
* appid: 9527
* 项目名：mushroom
* namespace：application（默认的）
* 配置项：
    * db.url: jdbc:mysql://localhost:3306/mushroom
    * db.username: root
    * db.password: mushroom
# 10. 客户端验证
本地使用Spring boot启动一个客户端进行验证，网上的例子很多，这里只简单验证了读取配置，见apollo-client。<br>
yml中区分了环境，启动的时候记得指定profile
# 11. 注意事项
* docker启动顺序有要求，先启动数据库在启动后台服务。后台服务要配置depends_on，依次启动config service、admin service、portal
* 服务端启动有点慢，具体原因还没有去研究，最慢的一次3分钟才启动好
* Macbook上的Docker软件对网络访问有限制，我是在mac上装了一个CentOS的虚拟机，然后在CentOS上跑Docker，Docker内的镜像内可以直接通过hostname互通，但是如果要通过Mac访问Docker服务，需要在mac上添加下路由转发（如果不是通过虚拟机跑Docker的可以忽略此项）
    * 设置hosts以便可以通过hostname访问Docker的服务
        ~~~
      172.20.2.1  mysql-apollo-config-dev
      172.20.2.2  mysql-apollo-config-prd
      172.20.2.3  mysql-apollo-portal-prd
      
      172.20.3.11 apollo-configservice-prd
      172.20.3.12 apollo-configservice-dev
      
      172.20.3.21 apollo-adminservice-prd
      172.20.3.22 apollo-adminservice-dev
      
      172.20.3.31 apollo-portal-prd
        ~~~
    * 设置路由转发，以便网络穿透到Docker
        ~~~
      sudo route add -net 172.20.0.0/16 10.211.55.4
        ~~~
      >10.211.55.4是CentOS虚拟机的ip地址
# 12 最后
欢迎交流。QQ：765260926


