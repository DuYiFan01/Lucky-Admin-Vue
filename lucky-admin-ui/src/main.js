import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss' // global css
import '@/assets/css/index.css'

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control
import { resetUserInfo } from '@/api/user'
import permission from './directive'
import { handleTree, checkPermission, handleMenuTree } from './utils/lucky'
/**
 * 如果你不想使用mock-server
 * 你想使用MockJs模拟api
 * 你可以执行：mockXHR（）
 * 目前MockJs将在生产环境中使用，请在上线前将其删除！！ ！
 */

if (process.env.NODE_ENV === 'production') {
  // const { mockXHR } = require('../mock')
  // mockXHR()
}

Vue.use(ElementUI)
// 自定义指令
Vue.use(permission)
// 自定义函数
Vue.prototype.handleTree = handleTree
Vue.prototype.handleMenuTree = handleMenuTree
Vue.prototype.checkPermission = checkPermission

// 全局按钮样式 操作栏按钮
Vue.prototype.buttonBar = {
  size: 'medium',
  plain: true,
  insertType: 'primary',
  deleteType: 'danger',
  importType: 'success',
  exportType: 'warning',
  reFreshType: 'info'
}
// 全局按钮样式 工具栏按钮
Vue.prototype.toolBar = {
  size: 'mini',
  insertType: 'text',
  updateType: 'text',
  deleteType: 'text',
  insertIcon: 'el-icon-plus',
  updateIcon: 'el-icon-edit',
  deleteIcon: 'el-icon-delete'
}

Vue.config.productionTip = false

Vue.prototype.resetUserInfo = resetUserInfo

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
