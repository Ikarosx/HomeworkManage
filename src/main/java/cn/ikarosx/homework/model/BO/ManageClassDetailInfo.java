package cn.ikarosx.homework.model.BO;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/16 12:56
 */
@Data
public class ManageClassDetailInfo {
  private String id;
  // 班级名称
  private String name;
  // 班级人数
  private Integer num;
  // 管理员信息
  private ManageClassDetailInfoUser adminUser;
  private Date createTime;
  private Date updateTime;
  // 班级同学
  private List<ManageClassDetailInfoUser> members;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getNum() {
    return num;
  }

  public void setNum(Integer num) {
    this.num = num;
  }

  public ManageClassDetailInfoUser getAdminUser() {
    return adminUser;
  }

  public void setAdminUser(ManageClassDetailInfoUser adminUser) {
    this.adminUser = adminUser;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public List<ManageClassDetailInfoUser> getMembers() {
    return members;
  }

  public void setMembers(List<ManageClassDetailInfoUser> members) {
    this.members = members;
  }
}
