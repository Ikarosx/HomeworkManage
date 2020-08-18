package cn.ikarosx.homework.model.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/17 15:47
 */
@Data
@ApiModel("用户查询作业详情,包含完成状态")
@AllArgsConstructor
public class ManageHomeworkDetails {
  @ApiModelProperty(value = "作业ID")
  private String id;

  @ApiModelProperty(value = "作业标题")
  private String title;

  @ApiModelProperty(value = "作业描述")
  private String description;

  @ApiModelProperty(value = "截止日期")
  private Date deadline;

  @ApiModelProperty(value = "是否完成")
  private Boolean finish;

  @ApiModelProperty(value = "创建时间", hidden = true)
  private Date createTime;

  @ApiModelProperty(value = "更新时间", hidden = true)
  private Date updateTime;
}
