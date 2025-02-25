import request from '@/utils/request'
const apiPath = '/tools/gen' // api Path url

// 查询列表信息
export function tableList(data, currentPage, pageSize) {
  return request({
    url: apiPath + '/tableList?&currentPage=' + currentPage + '&pageSize=' + pageSize,
    method: 'post',
    data
  })
}

// 生成代码预览
export function previewCode(data) {
  return request({
    url: apiPath + '/previewCode',
    method: 'post',
    data
  })
}

// 下载代码
export function downloadCode(data) {
  window.open(process.env.VUE_APP_BASE_API + apiPath + '/downloadCode?' +
    'tableName=' + data.tableName +
    '&packageName=' + data.packageName +
    '&mouldName=' + data.mouldName)
}

// 生成代码预览
export function getGenPo() {
  return request({
    url: apiPath + '/getGenPo',
    method: 'get'
  })
}
