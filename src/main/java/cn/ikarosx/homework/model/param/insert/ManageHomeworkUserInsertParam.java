package cn.ikarosx.homework.model.param.insert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/17 20:29
 */
@Data
@ApiModel(value = "作业提交记录Insert参数")
public class ManageHomeworkUserInsertParam {
  @ApiModelProperty(value = "作业ID")
  @NotBlank
  private String homeworkId;

  @NotBlank
  @ApiModelProperty(value = "文件id列表，逗号分隔")
  private String fileIds;
}
