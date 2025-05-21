<p align="center">
	<img alt="logo" src="https://doc.admin.anlucky.cn/images/logo.png" style="width: 100px;" />
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Lucky-Admin-Vue</h1>
<h4 align="center">基于SpringBoot3+Vue2前后端分离的快速开发后台的框架</h4>
<p align="center">
<a href='https://gitee.com/xiaodu6/lucky-admin-vue/stargazers'><img src='https://gitee.com/xiaodu6/lucky-admin-vue/badge/star.svg?theme=dark' alt='star'></img></a>
    <a href='https://gitee.com/xiaodu6/lucky-admin-vue/members'><img src='https://gitee.com/xiaodu6/lucky-admin-vue/badge/fork.svg?theme=dark' alt='fork'></img></a>
<a href='https://github.com/DuYiFan01/Lucky-Admin-Vue'><img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/DuYiFan01/Lucky-Admin-Vue"> </a>
<a href='https://github.com/DuYiFan01/Lucky-Admin-Vue'><img alt="GitHub forks" src="https://img.shields.io/github/forks/DuYiFan01/Lucky-Admin-Vue"> </a>

</p>


## 项目介绍
Lucky-Admin-Vue是一个基于SpringBoot3+Vue2前后端分离的快速开发后台的框架，采用前后端分离方式，集成了用户管理、菜单管理、角色管理、日志管理、操作日志、登录日志、代码生成、系统接口、在线用户、服务监控、缓存管理、文件上传、文件下载等功能模块。

## 文档地址

文档地址：https://doc.admin.anlucky.cn/

## 开发环境
- JDK 17
- MySQL 8.0+
- Node 18.20.5+
- Vue 2
- SpringBoot 3.4.1
- Mybatis-plus 3.5.10.1
- Springdoc OpenAPI 2.7.0

## 初始密码

- 账号：admin
- 密码：1111111

## 在线预览

在线预览：http://admin.anlucky.cn/#/index

## 目录结构

```
    > lucku-admin         ## 后端项目
    > lucku-admin-vue     ## 前端项目
```
### 1. 克隆项目到本地

```shell
git clone https://gitee.com/xiaodu6/lucky-admin-vue.git
```

### 2. 导入数据库
>数据库需要自己创建，数据库名称可以自定义
> 
>创建完成后，导入SQL文件
> 
>创建项目必须导入的SQL
> 
>数据库文件在根目录下： sql/sql/createProject.sql
> 
>移动端根据需要导入，若需要使用也要导入前端的SQL
> 
>移动端的SQL在根目录下：sql/sql/createApp.sql


### 3. 修改配置文件

>配置文件分为了 dev(开发环境)、prod(生产环境)，修改对应的配置文件中的内容可在不同环境中运行项目
> 
> 其中有3个配置文件需要修改：


    - application-dev.yml           ## 开发环境配置文件
    - application-prod.yml          ## 生产环境配置文件
    - application.yml               ## 公共配置文件

修改使用的配置文件

~~~yml
spring:
  application:
    name: lucky-admin
  profiles:
    active: dev ## dev 开发环境，prod 生产环境
~~~

不要忘记在对应的配置文件中修改对应的数据库连接信息


### 4. 前端安装依赖

```shell
cd lucky-admin-vue/lucky-admin-ui
npm install pnpm -g
pnpm install
```

### 5. 前端启动

```shell
npm run dev
```

### 6. 后端启动

### 7. 访问地址

```text
http://localhost:9528 ## 前端

http://localhost:8080/dev-api ## 开发环境后端

http://localhost:8080/prod-api ## 生产环境后端
```

## 
