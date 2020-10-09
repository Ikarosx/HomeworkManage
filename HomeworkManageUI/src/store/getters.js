const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  routes: state => state.user.routes,
  roles: state => state.user.roles,
  classId: state => state.user.classId,
  isClassAdmin: state => state.user.isClassAdmin,
  isAdmin: state => state.user.isAdmin,
}
export default getters
