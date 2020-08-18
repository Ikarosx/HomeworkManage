package cn.ikarosx.homework.model.param;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/15 20:38
 */
@Data
public class LoginParam {
  @NotBlank private String username;
  @NotBlank private String password;
}
