# springcloud-microservice-security
微服务权限，微服务的统一认证与鉴权<br />
spring gateway + nacos + sentinel + spring security + oauth2 + jwt + jpa<br />
采用nacos作为注册中心与配置中心，用于配置IP过滤的黑白名单，动态路由，服务的限流规则<br />
sppring gateway作为网关<br />
令牌采用RSA非对称加密对oauth2生成的token转化为JWT<br />
用redis定期更新缓存权限<br />
gateway-service: <br />
+ 负责动态路由<br />
+ 路由转发<br />
+ 服务限流<br />
+ 业务功能及菜单的角色鉴权<br />
+ IP终端过滤<br />
+ 忽略url<br />
user-auth-service:<br />
+ 用户认证<br />
+ JWT<br />
user-management-service:<br />
+ 基于RBAC的用户角色权限管理<br />
+ 缓存权限到redis<br />
nacos: 2.2.3<br />
mysql: 8<br />
redis: 6<br />
docker: 20.10.17<br />
skywalking: 9.2.0<br />
