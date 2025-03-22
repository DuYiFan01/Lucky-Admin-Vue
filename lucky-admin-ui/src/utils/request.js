import axios from 'axios'
import { MessageBox, Message, Loading } from 'element-ui'
import store from '@/store'
import { getToken, getTokenKeyName } from '@/utils/auth'
import { tansParams, blobValidate } from '@/utils/lucky'
import errorCode from '@/utils/errorCode'
import { saveAs } from 'file-saver'

let downloadLoadingInstance

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, //当跨域请求时发送cookie
  timeout: 30 * 1000 // 请求超时
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做一些事情
    if (store.getters.token) {
      // 让每个请求携带令牌
      // ['X-Token']是自定义标头密钥
      // 请根据实际情况修改
      config.headers[getTokenKeyName()] = getToken()
    }
    return config
  },
  error => {
    // 处理请求错误
    console.log('请求错误', error) // 用于调试
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  /**
   * 如果你想获得http信息，如标题或状态
   * 请返回 response => response
  */

  /**
   *通过自定义代码确定请求状态这里只是一个例子
   *也可以通过HTTP状态码来判断状态
   */
  response => {
    const res = response.data
    // 未登录状态码
    if (res.code === '-401') {
      // to re-login
      MessageBox.confirm('您的登录已失效,是否要重新登录', '确认注销', {
        confirmButtonText: '去登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('user/resetToken').then(() => {
          location.reload()
        })
      })
      return Promise.reject(new Error(res.message || 'Error'))
    }

    // 不是未登录状态
    // 如果Code 小于 0 表示失败 则弹出错误信息
    if (res.code < 0) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)
// 通用下载方法
export function download(url, params, filename, config) {
  downloadLoadingInstance = Loading.service({ text: '正在下载数据，请稍候', spinner: 'el-icon-loading', background: 'rgba(0, 0, 0, 0.7)' })
  return service.get(url, params, {
    transformRequest: [(params) => { return tansParams(params) }],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }).then(async(data) => {
    const isBlob = blobValidate(data)
    if (isBlob) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text()
      const rspObj = JSON.parse(resText)
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      Message.error(errMsg)
    }
    downloadLoadingInstance.close()
  }).catch((r) => {
    console.error(r)
    Message.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close()
  })
}
export default service
