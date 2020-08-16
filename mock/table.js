import Mock from 'mockjs'

const data = Mock.mock({
  'items|10': [{
    id: '@id',
    'identity|1-2': ['管理员', '学生', '老师']
    ,
    'state|1': ['正常', '未激活', '禁用'],
    'sex|1': ['男', '女'],
    studentId: /\d{11}/,
    userName: '@cword(2, 3)',
    nickName: '@cword(2, 3)',
    school: '@cword(2, 5)' + '学校'
  }]
})

export default [
  {
    url: '/vue-admin-template/table/list',
    type: 'get',
    response: config => {
      const items = data.items
      return {
        code: 20000,
        data: {
          total: items.length,
          items: items
        }
      }
    }
  }
]
