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
 * @date 2020-08-17 13:14:11
 */
@Data
@Entity
@Table(name = "manage_homework")
@GenericGenerator(name = "uuid", strategy = "uuid")
@EntityListeners(value = AuditingEntityListener.class)
@ApiModel(value = "ManageHomework")
public class ManageHomework implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid")
	@ApiModelProperty(value = "作业ID")
	private String id;

	@ApiModelProperty(value = "作业标题")
	private String title;

	@ApiModelProperty(value = "作业描述")
	private String description;

	@ApiModelProperty(value = "班级ID")
	private String classId;

	@ApiModelProperty(value = "截止日期")
	private Date deadline;

	@CreatedDate
	@ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;

	@LastModifiedDate
	@ApiModelProperty(value = "更新时间",hidden = true)
	private Date updateTime;

}
