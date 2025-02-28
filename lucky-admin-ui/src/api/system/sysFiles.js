import request from '@/utils/request'

const apiPath = '/system/sysFiles'

/**
 * 根据id查询
 * @param id
 * @returns {*}
 */
export function getById(id) {
  return request({
    url: apiPath + '/get/' + id,
    method: 'get'
  })
}
/**
 * 查询所有
 * @param {*} data
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
 * 查询所有
 * @param {*} data
 * @returns
 */
export function pageByParams(data, currentPage, pageSize) {
  return request({
    url: apiPath + '/pageByParams?currentPage=' + currentPage + '&pageSize=' + pageSize,
    method: 'post',
    data
  })
}

/**
 * 添加
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
 * 修改
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
 * 删除
 * @param {*} ids
 * @returns
 */
export function deleteByIds(ids) {
  return request({
    url: apiPath + '/delete/' + ids,
    method: 'get'
  })
}

/**
 * 获取所有文件业务类型
 * @returns {*}
 */
export function getFileBusinessType() {
  return request({
    url: apiPath + '/getFileBusinessType',
    method: 'get'
  })
}
