export const systemConfig = {
  bdApiUrl: "http://budingcc.cn:30000/api",
  imgUrl: "http://img.budingcc.cn/",
  openAuthenticate: false,
  openAuthorize: false
};

export const filters = {
  statusFilter(status) {
    const statusMap = {
      0: "success",
      1: "info",
      2: "danger"
    };
    return statusMap[status];
  },
  roleFilter(role) {
    const statusMap = {
      1: "success",
      2: "info",
      3: "danger"
    };
    return statusMap[role];
  },
  schoolStateFilter(state) {
    const statusMap = {
      0: "success",
      1: "danger"
    };
    return statusMap[state];
  },
  schoolSystemFilter(systemType) {
    const map = {
      1: "正方",
      2: "其他"
    };
    return map[systemType];
  }
};

export const roles = [
  {
    key: "0",
    label: "全部身份"
  },
  {
    value: 1,
    key: "1",
    label: "管理员"
  },
  {
    value: 2,
    key: "2",
    label: "学生"
  }
];

export const sex = [
  {
    key: "0",
    label: "全部性别"
  },
  {
    value: 0,
    key: "1",
    label: "男"
  },
  {
    value: 1,
    key: "2",
    label: "女"
  }
];

export const states = [
  {
    key: "0",
    label: "全部状态"
  },
  {
    key: "1",
    label: "正常",
    value: 0
  },
  {
    key: "2",
    label: "未激活",
    value: 1
  },
  {
    key: "3",
    label: "禁用",
    value: 2
  }
];

export const langExt = {
  hint: "点击，或拖动图片至此处",
  loading: "正在上传……",
  noSupported: "浏览器不支持该功能，请使用IE10以上或其他现在浏览器！",
  success: "上传成功",
  fail: "图片上传失败",
  preview: "头像预览",
  btn: {
    off: "取消",
    close: "关闭",
    back: "上一步",
    save: "保存"
  },
  error: {
    onlyImg: "仅限图片格式",
    outOfSize: "单文件大小不能超过 ",
    lowestPx: "图片最低像素为（宽*高）："
  }
};
