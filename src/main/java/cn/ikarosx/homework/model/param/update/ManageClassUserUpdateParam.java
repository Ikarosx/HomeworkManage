package cn.ikarosx.homework.model.param.update;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/16 14:24
 */
@Data
@ApiModel(value = "班级-成员Update参数")
public class ManageClassUserUpdateParam {
  @NotNull private String userId;
  @NotNull private String classId;
  @NotNull private Integer status;
}
