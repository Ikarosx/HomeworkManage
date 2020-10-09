package cn.ikarosx.homework.model.BO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/17 15:47
 */
@Data
@ApiModel("用户查询作业详情,包含完成状态")
public class ManageHomeworkDetails {
  @ApiModelProperty(value = "作业ID")
  private String id;

  @ApiModelProperty(value = "作业标题")
  private String title;

  @ApiModelProperty(value = "作业描述")
  private String description;

  @ApiModelProperty(value = "截止日期")
  private Date deadline;

  @ApiModelProperty(value = "是否完成")
  private Boolean finish;

  @ApiModelProperty(value = "创建时间", hidden = true)
  private Date createTime;

  @ApiModelProperty(value = "更新时间", hidden = true)
  private Date updateTime;

  public ManageHomeworkDetails(String id, String title, String description, Date deadline,
      Boolean finish, Date createTime, Date updateTime) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.deadline = deadline;
    this.finish = finish;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public Boolean getFinish() {
    return finish;
  }

  public void setFinish(Boolean finish) {
    this.finish = finish;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
