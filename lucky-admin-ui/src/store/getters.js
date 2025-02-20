const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  id: state => state.user.id,
  username: state => state.user.username,
  name: state => state.user.name,
  sex: state => state.user.sex,
  email: state => state.user.email,
  phone: state => state.user.phone,
  avatar: state => state.user.avatar,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes
}
export default getters
