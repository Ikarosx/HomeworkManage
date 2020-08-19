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
 * @date 2020-08-16 02:23:23
 */
@Data
@Entity
@Table(name = "manage_class_user")
@GenericGenerator(name = "uuid", strategy = "uuid")
@EntityListeners(value = AuditingEntityListener.class)
@ApiModel(value = "ManageClassUser")
public class ManageClassUser implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid")
	@ApiModelProperty(value = "班级成员关系表ID")
	private String id;

	@ApiModelProperty(value = "班级ID")
	private String classId;

	@ApiModelProperty(value = "用户ID")
	private String userId;

	@ApiModelProperty(value = "状态",notes = "0：已加入，1：等待，2：被拒绝")
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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
