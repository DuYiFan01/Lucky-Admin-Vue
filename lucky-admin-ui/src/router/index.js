import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

/* Layout */
import Layout from '@/layout'
/**
 * 注意：子菜单仅在路线子长度>=1时出现
 * 详见：https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden：                       如果设置为true，则项目不会显示在侧边栏中（默认值为false）
 * alwaysShow:true                如果设置为true，将始终显示根菜单
 *                                如果未设置alwaysShow，则当项目有多个子路由时，
 *                                 它将变为嵌套模式，否则不显示根菜单
 * redirect：noRedirect           如果设置了no重定向，则no重定向不会在面包屑中重定向
 * name:'router-name'             该名称由<keep-alive>使用（必须设置！！！）
 * meta：{
      roles['admin'，'editor']      控制页面角色（可以设置多个角色）
      title:'title'                 名称显示在侧边栏和面包屑中（推荐设置）
      icon：'svg-name'/'el-icon-x'  图标显示在侧边栏中
      breadcrumb：false             如果设置为false，则该项将隐藏在面包屑中（默认为true）
      activeMenu:'/example.list'    如果设置了路径，侧边栏将突出显示您设置的路径
    }
 */

/**
 *
 * 基础路由
 * 没有权限要求的基页
 * 可以访问所有角色
*/
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/', // 根路由
    component: Layout, // 引入布局文件
    redirect: '/index', // 根路由默认跳转
    children: [
      {
        path: '/index', // 路由地址
        name: 'index', // 路由名称
        component: () => import('@/views/dashboard/index'), // 路由组件
        meta: { title: '首页', icon: 'dashboard' } // 路由元信息
      },
      {
        path: '/profile',
        name: 'profile',
        hidden: true,
        component: () => import('@/views/profile/index'),
        meta: { title: '个人中心', icon: 'dashboard' }
      }
    ]
  }
]

/**
 * 异步路由
 * 需要根据用户角色动态加载的路由
 */
export const asyncRoutes = []

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
