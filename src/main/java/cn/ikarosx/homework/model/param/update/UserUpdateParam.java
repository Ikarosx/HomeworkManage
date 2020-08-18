package cn.ikarosx.homework.model.param.update;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@Data
@ApiModel(value = "用户Update参数")
public class UserUpdateParam {

  @NotBlank private String id;
  @NotBlank private String password;
  private String nickname;
}
