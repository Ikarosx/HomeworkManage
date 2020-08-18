package cn.ikarosx.homework.model.param.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/17 13:21
 */
@Data
@ApiModel(value = "作业Update参数")
public class ManageHomeworkUpdateParam {
  @ApiModelProperty(value = "作业ID")
  @NotNull
  private String id;

  @ApiModelProperty(value = "作业标题")
  private String title;

  @ApiModelProperty(value = "作业描述")
  private String description;

  @ApiModelProperty(value = "截止日期")
  private Date deadline;
}
