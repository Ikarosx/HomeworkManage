package cn.ikarosx.homework.model.param.query;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/18 19:29
 */
@Data
@ApiModel("查询作业完成情况参数")
public class HomeworkFinishInfoQueryParam {
  @NotBlank private String classId;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }
}
