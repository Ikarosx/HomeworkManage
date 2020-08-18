package cn.ikarosx.homework.constant;

/**
 * 班级用户状态Enum
 *
 * @author Ikarosx
 * @date 2020/8/16 15:55
 */
public enum ManageClassUserStatusEnum {
  // 已经加入
  Joined(0),
  // 等待通过
  Waiting(1),
  // 被拒绝
  Rejected(2);

  private Integer status;

  public Integer getStatus() {
    return status;
  }

  ManageClassUserStatusEnum(Integer status) {
    this.status = status;
  }
}
