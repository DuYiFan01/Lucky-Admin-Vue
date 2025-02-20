import permission from './permission/permission'
// import clipboard from './module/clipboard'

const install = function(Vue) {
  Vue.directive('permission', permission)
  // Vue.directive('clipboard', clipboard)
}

if (window.Vue) {
  window['permission'] = permission
  Vue.use(install); // eslint-disable-line
}

permission.install = install
export default permission
