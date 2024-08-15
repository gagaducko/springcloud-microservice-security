# springcloud-microservice-security
微服务权限，微服务的统一认证与鉴权 
spring gateway + nacos + sentinel + spring security + oauth2 + jwt + jpa 
采用nacos作为注册中心与配置中心，用于配置IP过滤的黑白名单，动态路由，服务的限流规则 
sppring gateway作为网关 
令牌采用RSA非对称加密对oauth2生成的token转化为JWT 
用redis定期更新缓存权限 
gateway-service:  
+ 负责动态路由 
+ 路由转发 
+ 服务限流 
+ 业务功能及菜单的角色鉴权 
+ IP终端过滤 
+ 忽略url 
user-auth-service: 
+ 用户认证 
+ JWT 
user-management-service: 
+ 基于RBAC的用户角色权限管理 
+ 缓存权限到redis 
nacos: 2.2.3 
mysql: 8 
redis: 6 
docker: 20.10.17 
skywalking: 9.2.0 
