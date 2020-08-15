package cn.ikarosx.homework.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Ikarosx
 * @date 2020-08-15 01:42:01
 */
@Data
@Entity
@Table(name = "manage_user")
@GenericGenerator(name = "uuid", strategy = "uuid")
@EntityListeners(value = AuditingEntityListener.class)
@ApiModel(value = "ManageUser")
public class User implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid")
  @ApiModelProperty(value = "用户ID")
  private String id;

  @ApiModelProperty(value = "用户名")
  private String username;

  @ApiModelProperty(value = "密码")
  private String password;

  @ApiModelProperty(value = "0:普通用户, 1:系统管理员", hidden = true)
  private Integer type;

  @ApiModelProperty(value = "所属班级ID")
  private String classId;

  @CreatedDate
  @ApiModelProperty(value = "创建时间", hidden = true)
  private Date createTime;

  @LastModifiedDate
  @ApiModelProperty(value = "修改时间", hidden = true)
  private Date updateTime;
}
