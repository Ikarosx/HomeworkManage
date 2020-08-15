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
}
