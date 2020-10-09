package cn.ikarosx.homework.model.excel;

import java.util.Date;
import lombok.Data;

/**
 * 导出作业完成情况Excel对象
 *
 * @author Ikarosx
 * @date 2020/8/18 20:20
 */
@Data
public class HomeworkFinishInfoExcelModel {
  @ExcelAnnotation(columnName = "学号")
  private String studentNo;

  @ExcelAnnotation(columnName = "名称")
  private String nickname;

  @ExcelAnnotation(columnName = "是否完成")
  private String status;

  @ExcelAnnotation(columnName = "完成时间")
  private Date createTime;

  public String getStudentNo() {
    return studentNo;
  }

  public void setStudentNo(String studentNo) {
    this.studentNo = studentNo;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
