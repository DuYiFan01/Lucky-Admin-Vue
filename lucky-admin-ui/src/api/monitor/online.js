import request from '@/utils/request'

const apiPath = '/monitor/online'


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
 * 强制下线
 * @param {*} token
 * @returns
 */
export function forceLogout(token) {
    return request({
      url: apiPath + '/forceLogout?token=' + token,
      method: 'get'
    })
  }