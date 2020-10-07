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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Ikarosx
 * @date 2020-09-16 21:38:23
 */
@Data
@Entity
@Table(name = "manage_homework_file")
@GenericGenerator(name = "uuid", strategy = "uuid")
@EntityListeners(value = AuditingEntityListener.class)
@ApiModel(value = "ManageHomeworkFile")
public class ManageHomeworkFile implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid")
  private String id;

  @ApiModelProperty(value = "作业ID")
  private String homeworkUserId;

  @ApiModelProperty(value = "文件ID")
  private String fileId;

  @ApiModelProperty(value = "文件名称")
  private String fileName;

  @CreatedDate
  @ApiModelProperty(value = "创建时间", hidden = true)
  private Date createTime;
}
