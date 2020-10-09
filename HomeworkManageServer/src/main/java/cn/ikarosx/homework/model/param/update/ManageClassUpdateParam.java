package cn.ikarosx.homework.model.param.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@Data
@ApiModel(value = "班级Update参数")
public class ManageClassUpdateParam {
  private String id;

  @ApiModelProperty("班级名称")
  private String name;

  @ApiModelProperty("班级管理员ID")
  private String adminUserId;

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

  public String getAdminUserId() {
    return adminUserId;
  }

  public void setAdminUserId(String adminUserId) {
    this.adminUserId = adminUserId;
  }
}
