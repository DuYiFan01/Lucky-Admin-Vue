import request from '@/utils/request'

const apiPath = '/system/sysMenus'

/**
 * 查询所有菜单
 * @param {*} data title 菜单名称
 * @returns
 */
export function list(data) {
  return request({
    url: apiPath + '/list',
    method: 'post',
    data
  })
}
/**
 * 查询所有App菜单
 * @param {*} data title 菜单名称
 * @returns
 */
export function listApp(data) {
  return request({
    url: apiPath + '/listApp',
    method: 'post',
    data
  })
}

/**
 * 添加菜单
 * @param {*} data
 * @returns
 */
export function save(data) {
  return request({
    url: apiPath + '/save',
    method: 'post',
    data
  })
}

/**
 * 修改菜单
 * @param {*} data
 * @returns
 */
export function updateById(data) {
  return request({
    url: apiPath + '/updateById',
    method: 'post',
    data
  })
}

/**
 * 删除菜单
 * @param {*} ids
 * @returns
 */
export function deleteByIds(ids) {
  return request({
    url: apiPath + '/delete/' + ids,
    method: 'get'
  })
}
