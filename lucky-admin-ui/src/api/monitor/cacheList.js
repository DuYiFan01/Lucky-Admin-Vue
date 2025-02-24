import request from '@/utils/request'

const apiPath = '/monitor/cacheList'


/**
 * 查询所有缓存名称
 * @param {*} data
 * @returns
 */
export function nameList() {
    return request({
      url: apiPath + '/nameList',
      method: 'get',
    })
}

/**
 * 获取缓存Key列表
 * @param {*} data
 * @returns
 */
export function keyListByCacheName(cacheName) {
    return request({
      url: apiPath + '/keyList/' + cacheName,
      method: 'get',
    })
}


/**
 * 获取缓存值
 * @param {*} data
 * @returns
 */
export function cacheValueByKey(cacheName,cacheKey) {
    return request({
      url: apiPath + '/cacheValue/' + cacheName+'/'+cacheKey,
      method: 'get',
    })
}

/**
 * 删除指定名字的缓存
 * @param {*} data
 * @returns
 */
export function deleteCacheNameByCacheName(cacheName) {
    return request({
      url: apiPath + '/deleteCacheName/'+cacheName,
      method: 'get',
    })
}


/**
 * 删除指定Key缓存
 * @param {*} data
 * @returns
 */
export function deleteCacheKeyByCacheKey(cacheKey) {
    return request({
      url: apiPath + '/deleteCacheKey/'+cacheKey,
      method: 'get',
    })
}

/**
 * 清空缓存
 * @param {*} data
 * @returns
 */
export function deleteCacheAll() {
    return request({
      url: apiPath + '/deleteCacheAll',
      method: 'get',
    })
}