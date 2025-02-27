import request from '@/utils/request'
import store from '@/store'

const apiPath = '/user'
/**
 * 登录
 * @param {*} data
 * @returns
 */
export function login(data) {
  return request({
    url: apiPath + '/login',
    method: 'post',
    data
  })
}
/**
 * 获取用户信息
 * @param {*} token
 * @returns
 */
export function getInfo(token) {
  return request({
    url: apiPath + '/info',
    method: 'get',
    params: { token }
  })
}
/**
 * 退出登录
 * @returns
 */
export function logout() {
  return request({
    url: apiPath + '/logout',
    method: 'get'
  })
}

/**
 * 获取验证码
 * @returns captchaEnabled  uuid  image
 */
export function getCode() {
  return request({
    url: apiPath + '/getCode',
    method: 'get'
  })
}

/**
 * 修改用户信息
 * @param {*} data
 * @returns
 */
export function updateInfo(data) {
  return request({
    url: apiPath + '/updateInfo',
    method: 'post',
    data
  })
}

/**
 * 修改密码
 * @param {*} data
 * @returns
 */
export function updatePassword(data) {
  return request({
    url: apiPath + '/updatePassword',
    method: 'post',
    data
  })
}

/**
 * 获取路由
 * @param {*} roles
 */
export function getRouters(roles) {
  return request({
    url: apiPath + '/getRouters',
    method: 'get'
  })
}

/**
 * 重置用户信息
 */
export function resetUserInfo() {
  store.dispatch('user/getInfo')
  return true
}

/**
 * 获取用户可分配角色信息
 */
export function getAuthUser(userId) {
  return request({
    url: apiPath + '/getAuthUser/' + userId,
    method: 'get'
  })
}
/**
 * 用户-分配角色
 */
export function saveAuthUser(userId, data) {
  return request({
    url: apiPath + '/saveAuthUser/' + userId,
    method: 'post',
    data
  })
}
/**
 * 获取角色可分配的用户
 */
export function getAuthRole(roleId) {
  return request({
    url: apiPath + '/getAuthRole/' + roleId,
    method: 'get'
  })
}
/**
 * 角色-分配用户
 */
export function saveAuthRole(roleId, data) {
  return request({
    url: apiPath + '/saveAuthRole/' + roleId,
    method: 'post',
    data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: apiPath + '/avatar',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: data
  })
}
