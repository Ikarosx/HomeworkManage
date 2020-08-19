package cn.ikarosx.homework.model.BO;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020-08-15 01:42:01
 */
@Data
public class ManageClassDetailInfoUser implements Serializable {
  private String id;
  private String username;
  private String nickname;
  private String studentNo;
  private Date createTime;

  public ManageClassDetailInfoUser(String id, String username, String nickname, String studentNo,
      Date createTime) {
    this.id = id;
    this.username = username;
    this.nickname = nickname;
    this.studentNo = studentNo;
    this.createTime = createTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
