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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getStudentNo() {
    return studentNo;
  }

  public void setStudentNo(String studentNo) {
    this.studentNo = studentNo;
  }
}
