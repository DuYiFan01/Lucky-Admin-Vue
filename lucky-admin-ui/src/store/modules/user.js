import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    id: '', // 用户ID
    username: '', // 用户名账号
    name: '', // 昵称
    sex: '', // 性别
    email: '', // 邮箱
    phone: '', // 手机号
    avatar: '', // 头像url
    roles: [], // 角色
    permissions: [] // 按钮权限
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_ID: (state, id) => {
    state.id = id
  },
  SET_USERNAME: (state, username) => {
    state.username = username
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_SEX: (state, sex) => {
    state.sex = sex
  },
  SET_EMAIL: (state, email) => {
    state.email = email
  },
  SET_PHONE: (state, phone) => {
    state.phone = phone
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar === null ? require('@/assets/images/avatar/profile.jpg') : avatar
    // state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_PERMISSIONS: (state, permissions) => {
    state.permissions = permissions
  }
}

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
    const { username, password, uuid, code } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password, uuid, code }).then(response => {
        const { data } = response
        // console.log(data)
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response
        if (!data) {
          reject('验证失败，请重新登录。')
        }
        const { id, username, name, sex, email, phone, avatar, roles, permissions } = data
        // Roles必须为非空数组
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles必须是非空数组!')
        }
        commit('SET_ID', id)
        commit('SET_USERNAME', username)
        commit('SET_NAME', name)
        commit('SET_SEX', sex)
        commit('SET_EMAIL', email)
        commit('SET_PHONE', phone)
        commit('SET_AVATAR', avatar)
        commit('SET_ROLES', roles)
        commit('SET_PERMISSIONS', permissions)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

