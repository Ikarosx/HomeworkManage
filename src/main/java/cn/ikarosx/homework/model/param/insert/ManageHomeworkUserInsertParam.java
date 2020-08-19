package cn.ikarosx.homework.model.param.insert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/17 20:29
 */
@Data
@ApiModel(value = "作业提交记录Insert参数")
public class ManageHomeworkUserInsertParam {
  @ApiModelProperty(value = "作业ID")
  @NotNull
  private String homeworkId;

  @NotNull
  @ApiModelProperty(value = "内容")
  private String content;

  public String getHomeworkId() {
    return homeworkId;
  }

  public void setHomeworkId(String homeworkId) {
    this.homeworkId = homeworkId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
