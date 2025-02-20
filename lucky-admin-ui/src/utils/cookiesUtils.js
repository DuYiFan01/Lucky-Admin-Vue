import Cookies from 'js-cookie'

/**
 * 获取Cookies
 * @param {*} key
 * @returns
 */
export function getMap(key) {
  return Cookies.get(key)
}
/**
 * 添加Cookies
 * @param {*} key
 * @param {*} value
 * @returns
 */
export function setMap(key, value) {
  return Cookies.set(key, value)
}

/**
 * 删除Cookies
 * @param {*} key
 * @returns
 */
export function removeMap(key) {
  return Cookies.remove(key)
}
