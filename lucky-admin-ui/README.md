# Lucky-Admin-Ui

## 项目介绍
    Lucky-Admin-Ui是一个基于vue2.0+element-ui+vuex+vue-router+axios的前后端分离后台管理系统框架
## 框架功能
    > 权限管理
    > 菜单管理
    > 用户管理
    > 角色管理
    > 定时任务
    > 代码生成
    > 登录日志
    > 操作日志
    > 图片上传
    > 文件上传
## 项目运行
    1.下载项目
    2.npm install
    3.npm run dev
    4.访问地址：http://localhost:9528

## 修改关键内容
### 1.修改后台接口地址
    修改.env.development文件中的VUE_APP_BASE_API地址为后台地址 // 开发环境
    修改.env.production文件中的VUE_APP_BASE_API地址为后台地址 // 生产环境
### 2.修改代理地址
    修改vue.config.js文件中的devServer.proxy地址为后台地

> 注意：这里的代理只在开发环境生效，生产环境请使用 **nginx** 代理或其他方式解决
### 3.修改用户下拉菜单项目
    代码文件位置：src\layout\components\Navbar.vue
### 4.登录/注销登录/获取用户信息/修改用户信息/获取验证码/获取路由/修改密码
    代码文件位置：src\api\user.js
    
    默认路由相关的API基本上都在这里，随着时间增长可能会添加新的API或增加别的API文件，请在对应页面中去寻找实际所在的位置，但不出意外的话都会在这
### 5.修改默认头像
    src\assets\images\avatar\profile.jpg
    若要修改请添加自己的图片，并改名为 profile.jpg 或 修改源代码中的头像路径
