package cn.ikarosx.homework.model.param.insert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/17 13:21
 */
@Data
@ApiModel(value = "作业Insert参数")
public class ManageHomeworkInsertParam {

  @ApiModelProperty(value = "作业标题")
  private String title;

  @ApiModelProperty(value = "作业描述")
  private String description;

  @ApiModelProperty(value = "班级ID")
  @NotBlank
  private String classId;

  @ApiModelProperty(value = "截止日期")
  private Date deadline;
}
