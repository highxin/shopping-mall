# shopping-mall
项目概览
cloud-mall 是一个基于 Spring Cloud Alibaba 微服务架构的电商商城后端项目。它采用标准的微服务拆分 + 分布式注册中心/配置中心 + 网关路由的模式，把电商核心业务（商品、订单、购物车、用户）拆分成独立可部署的服务。

基础框架：Spring Boot 3.3.5 + Spring Cloud 2023.0.3 + Spring Cloud Alibaba 2023.0.3.2
JDK：17
构建工具：Maven（聚合多模块工程）
模块划分
父工程 cloud-mall 是一个 pom 聚合工程，包含 7 个子模块：

模块
mall-common	公共模块，提供通用工具与基础组件（详见下方）
mall-gateway	API 网关，统一入口
mall-product	商品服务（分类 / 品牌 / SKU）
mall-order	订单服务（下单、订单信息、状态流转）
mall-user	用户服务（目录下可见，代码结构较精简）
mall-cart	购物车服务
mall-search 搜索服务
