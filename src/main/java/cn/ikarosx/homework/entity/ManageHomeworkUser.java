package cn.ikarosx.homework.entity;
import lombok.Data;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import cn.ikarosx.homework.entity.jpa.Update;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;

/**
 * @author Ikarosx
 * @date 2020-08-17 15:59:34
 */
@Data
@Entity
@Table(name = "manage_homework_user")
@GenericGenerator(name = "uuid", strategy = "uuid")
@EntityListeners(value = AuditingEntityListener.class)
@ApiModel(value = "ManageHomeworkUser")
public class ManageHomeworkUser implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid")
	@ApiModelProperty(value = "用户作业ID")
	private String id;

	@ApiModelProperty(value = "用户ID")
	private String userId;

	@ApiModelProperty(value = "作业ID")
	private String homeworkId;

	@ApiModelProperty(value = "内容")
	private String content;

	@ApiModelProperty(value = "0未完成 1完成")
	private Integer status;

	@CreatedDate
	@ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;

	@LastModifiedDate
	@ApiModelProperty(value = "更新时间",hidden = true)
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(String homeworkId) {
		this.homeworkId = homeworkId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
