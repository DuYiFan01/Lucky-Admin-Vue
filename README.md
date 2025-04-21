<p align="center">
	<img alt="logo" src="logo.png" style="width: 100px;" />
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Lucky-Admin-Vue</h1>
<h4 align="center">基于SpringBoot3+Vue2前后端分离的快速开发后台的框架</h4>
<p align="center">
<a href='https://gitee.com/xiaodu6/lucky-admin-vue/stargazers'><img src='https://gitee.com/xiaodu6/lucky-admin-vue/badge/star.svg?theme=dark' alt='star'></img></a>
    <a href='https://gitee.com/xiaodu6/lucky-admin-vue/members'><img src='https://gitee.com/xiaodu6/lucky-admin-vue/badge/fork.svg?theme=dark' alt='fork'></img></a>
</p>


## 项目介绍
Lucky-Admin-Vue是一个基于SpringBoot3+Vue2前后端分离的快速开发后台的框架，采用前后端分离方式，集成了用户管理、菜单管理、角色管理、日志管理、操作日志、登录日志、代码生成、系统接口、在线用户、服务监控、缓存管理、文件上传、文件下载等功能模块。
## 开发环境
- JDK 17
- MySQL 8.0+
- Node 18.20.5+
- Vue 2
- SpringBoot 3.4.1
- Mybatis-plus 3.5.10.1
- Springdoc OpenAPI 2.7.0

## 目录结构
在线预览：http://admin.anlucky.cn/#/index
```
    > lucku-admin         ## 后端项目
    > lucku-admin-vue     ## 前端项目
```

## 后端目录结构
```
main
├─config            ## 全局配置
├─exception         ## 全局异常处理
├─gen               ## 代码生成相关
│  ├─config         ## 代码生成配置
│  └─service        ## 代码生成接口
│      └─impl       ## 代码生成接口实现
├─system            ## 系统管理相关
│  ├─annotation     ## 自定义注解
│  ├─aspectj        ## 切面
│  ├─base           ## 基础类
│  │  └─controller  ## 控制器
│  ├─config         ## 系统配置
│  ├─constant       ## 系统常量
│  ├─controller     ## 控制器
│  ├─enums          ## 枚举
│  ├─filter         ## 过滤器
│  ├─manager        ## 管理类
│  │  └─factory     ## 工厂
│  ├─mapper         ## 数据层
│  ├─pojo           ## 实体类
│  ├─service        ## 业务层
│  │  └─impl        ## 业务层实现
│  └─vo             ## 视图对象
├─utils             ## 工具类
└─vo                ## 视图对象
```
## 前端目录结构
```
main
├─api               ## 接口
├─assets            ## 静态资源
├─components        ## 组件
├─directive         ## 指令
├─icons             ## 图标
│  └─svg            ## svg图标
├─layout            ## 布局
├─router            ## 路由
├─store             ## 状态管理
├─styles            ## 样式
├─utils             ## 工具类
└─views             ## 页面

```
## 快速开始
1. 克隆项目到本地
2. 修改数据源配置
3. 执行sql文件
4. 运行项目

## 待优化内容
1. 菜单支持添加内链iFrame
2. 集成Excel导入导出
3. 集成文件管理系统(本地、七牛云...)
4. 集成定时任务系统
5. 微信小程序开发

## 配置移动端
1. 执行完 /sql/createProject.sql 文件的基础上，执行文件 /sql/createApp.sql 文件
2. 打开 PC-Web端，查看菜单中是否包含对应的APP管理菜单和APP角色菜单
3. APP管理-> APP菜单-> 添加菜单

## 项目截图
![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)
![img_6.png](img_6.png)
![img_7.png](img_7.png)
![img_8.png](img_8.png)
![img_9.png](img_9.png)
![img_10.png](img_10.png)
![img_11.png](img_11.png)
![img_12.png](img_12.png)
![img_13.png](img_13.png)
![img_14.png](img_14.png)
![img_16.png](img_16.png)

## 致谢
框架中许多思路构建来源于若依框架，感谢原作者。

## BUG交流QQ群
![img_15.png](img_15.png)

## 常见问题？

### 生产环境文件上传失败
在生产环境中，大多数上传失败的原因为权限不足的问题，请检查对应目录是否有写入的权限

### 在线用户统计不准确
在线用户统计主要统计于登录的COOKIE，由于浏览器的缓存机制，COOKIE在浏览器关闭后失效，或人为清除COOIKE，会导致在Redis当中存储的在线用户的COOKIES不匹配，导致在线用户统计会不准确

### 文件列表数据对不上
文件上传有如下几种情况：
1. 文件上传成功，数据库保存失败，导致文件存在，但是数据库中不存在，此时文件列表数据对不上(解决方式：添加try cach 捕获到异常后删除文件)
2. 文件上传失败，数据库保存成功，导致文件不存在，但是数据库中存在，此时文件列表数据对不上(添加事务 @Transactional 可解决)

### 文件上传过慢
可能是文件上传过慢的原因是：
1. 服务器配置过低，如内存不足，CPU过低，磁盘过低等
2. 文件过大，上传时间过长(限制上传文件大小)
3. 文件过大，上传时间过长(文件上传添加了文件哈希校验，获得文件哈希需读取一遍文件，若文件过大会导致这里运行的时间过长，可通过修改文件哈希算法解决，或减少文件上传大小)

> 文件上传接口实现：src/main/java/cn/anlucky/luckyadmin/system/service/impl/SysFilesServiceImp.java
> 
> 文件上传工具：cn/anlucky/luckyadmin/utils/file/FileUploadUtils.java
> 
> 注意：文件上传工具类当中，是提供了上传文件，并未提供上传文件后的处理，上传文件成功后保存数据库的操作在接口实现当中

### 针对于txt文件下载为浏览器打开的解决方案
问题描述：

1. txt文件下载为浏览器打开，部分浏览器打开时会出现乱码，乱码原因为浏览器默认以UTF-8编码打开，而txt文件编码为GBK，所以需要解决乱码问题
2. PDF文件下载为浏览器打开
3. 其他类型文件被浏览器识别打开的情况

解决方式：
1. 框架整体提供的文件输出方式为 file: 协议输出，在被浏览器识别时本质上就是本机使用了浏览器打开了某一文件，若想保存到本地，使用浏览器保存文件( **Ctrl + S** ) 保存下载文件即可，此方式不会乱码
2. 前端框架提供了download方式，这种方式提供在了 request.js 中的download 函数，传入参数即可保存

### 文件删除
文件删除使用了逻辑删除并未真正删除文件和数据库的内容，(此方式比较常见)，使用MybatisPlus的逻辑删除功能

文件删除数据库使用了逻辑删除，文件没有逻辑删除的选项，我们系统针对于本地存储的文件删除，使用了移动的方式，具体可以查询代码

cn/anlucky/luckyadmin/utils/file/FileUploadUtils.java

当然，我们也提供了物理删除的方式，查询代码修改配置即可实现自动切换移动和物理删除方式

### 自定义全局工具类
src/main/java/cn/anlucky/luckyadmin/config/LuckyConfig.java 全局配置类

全局配置类提供了全局的设置，SpringBoot启动后会自动注入在application.yml中Lucky开头的配置，若您想自定义或新增配置，请按照如下方式添加
1. 在application.yml当中添加对应的配置
2. 创建对应的Config配置类
3. 添加组件注解，将bean对象交给Sping IOC进行管理自动注入
4. 添加对应属性，添加属性时，请按照驼峰命名法
5. 创建对应的Set方法(注意，使用Static修饰的属性无法被自动注入，框架采用使用私有set方式注入，并赋值的方式，参考LuckyConfig进行自定义)

### APP端动态菜单
APP端实现了动态菜单，但路由菜单是被page.json管理的
所以添加APP菜单时，路由信息因为：page.json当中的path对应的值，若要修改，请修改page.json当中的Path并修改菜单路由信息

### APP端菜单添加
移动端动态菜单添加时，只允许一级目录，目录下菜单，菜单下按钮，不允许出现多级，否则可能会出现不可预估的错误BUG，若有对应的多级目录需求，请联系作者

移动端菜单和PC端菜单是在同一张表当中，前端当中增加的HandleMenuTree函数进行格式化对应的数据为Tree,详情可以去了解/utils/lucky.js 工具类中的 handleMenuTree 方法

移动端菜单和PC端菜单是在同一张表当中，移动端的APP菜单的根父路径ID为 -1 (parent_id = -1)
### APP端角色修改
APP端角色修改时，采用了同一个角色列表，但是在不同的页面当中配置不同的角色权限
详细为：
在APP角色页面当中，**列表编辑** 按钮点击后，弹出来的菜单配置为移动端的菜单配置
在PC角色管理页面当中 **列表编辑** 按钮点击后，弹出来的菜单配置为PC端的菜单配置
因他俩是同一个角色，只是在不同的页面当中查询了不同的菜单，所以对应用户分配角色时，他也会拥有对应的PC端的角色(因为是同一个角色，若PC没有菜单权限则就没有对应的权限)

### 前端代码格式修复
所有的配置文件都在 .eslintrc.js 中。 本项目基本规范是依托于 vue 官方的 eslint 规则 eslint-config-vue 做了少许的修改。大家可以按照自己的需求进行定制化配置。

执行下方代码，会自动修复一些简单的代码格式
```
npm run lint -- --fix

```
### 取消ESLINT检查
如果你不想使用 ESLint 校验（不推荐取消），只要找到 vue.config.js 文件。 进行如下设置 lintOnSave: false 即可。

