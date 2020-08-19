package cn.ikarosx.homework.model.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/18 19:36
 */
@Data
@ApiModel("作业完成情况")
public class HomeworkFinishInfo {
  @ApiModelProperty("昵称")
  private String nickname;

  @ApiModelProperty("学号")
  private String studentNo;

  @ApiModelProperty("完成状态")
  private Integer status;

  @ApiModelProperty("完成时间")
  private Date createTime;

  public HomeworkFinishInfo(String nickname, String studentNo, Integer status, Date createTime) {
    this.nickname = nickname;
    this.studentNo = studentNo;
    this.status = status;
    this.createTime = createTime;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
