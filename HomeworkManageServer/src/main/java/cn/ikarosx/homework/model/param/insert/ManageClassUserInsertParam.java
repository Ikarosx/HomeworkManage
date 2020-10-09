package cn.ikarosx.homework.model.param.insert;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/16 14:24
 */
@Data
@ApiModel(value = "班级-成员Insert参数")
public class ManageClassUserInsertParam {
  @NotNull private String classId;

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }
}
