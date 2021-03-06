package cn.ikarosx.homework.model.param.insert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@Data
@ApiModel(value = "班级Insert参数")
public class ManageClassInsertParam {
  @ApiModelProperty(value = "班级名称")
  @NotBlank
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
