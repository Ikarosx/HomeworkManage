package cn.ikarosx.homework.model.param.insert;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@Data
@ApiModel(value = "用户Insert参数")
public class UserInsertParam {
  @NotBlank private String username;
  @NotBlank private String password;
  @NotBlank private String nickname;
  @NotBlank private String studentNo;
}
