import { getRouters } from '@/api/user'
import { constantRoutes } from '@/router'
import Layout from '@/layout'
import ParentView from '@/components/ParentView/index.vue'
/**
 * 使用 meta.role 确定当前用户是否具有权限
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 通过递归过滤异步路由表
 * @param routes 动态路由
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

/**
 * 格式化路由
 * @param {*} params
 */
export function formartRouter(routers) {
  const routersTree = []
  for (let i = 0; i < routers.length; i++) {
    const e = routers[i]
    if (e.component) {
      if (e.component === 'Layout') {
        e.component = Layout
      } else if (e.component === 'ParentView') {
        e.component = ParentView
      } else {
        const component = e.component
        e.component = (re) => require([`@/views${component}`], re)
      }
    }
    if (e.children && e.children.length > 0) {
      e.children = formartRouter(e.children)
    }
    routersTree.push(e)
  }
  return routersTree
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      getRouters(roles).then(res => {
        const { data } = res
        accessedRoutes = formartRouter(data)
        accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      }).catch(error => {
        console.error('generateRoutes error', error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
