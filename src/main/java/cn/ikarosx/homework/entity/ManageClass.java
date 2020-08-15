package cn.ikarosx.homework.entity;

import cn.ikarosx.homework.entity.jpa.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@Table(name = "manage_class")
@GenericGenerator(name = "uuid", strategy = "uuid")
@EntityListeners(value = AuditingEntityListener.class)
@ApiModel(value = "ManageClass")
public class ManageClass implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid")
  @ApiModelProperty(value = "班级ID")
  private String id;

  @ApiModelProperty(value = "班级名称")
  private String name;

  @ApiModelProperty(value = "班级管理员用户ID")
  private String adminUserId;

  @CreatedDate
  @ApiModelProperty(value = "创建时间", hidden = true)
  private Date createTime;

  @LastModifiedDate
  @ApiModelProperty(value = "修改时间", hidden = true)
  private Date updateTime;
}
