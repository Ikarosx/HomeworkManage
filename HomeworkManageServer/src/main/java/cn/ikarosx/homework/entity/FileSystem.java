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
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Ikarosx
 * @date 2020-09-16 10:23:58
 */
@Data
@Entity
@Table(name = "t_file_system")
@GenericGenerator(name = "uuid", strategy = "uuid")
@EntityListeners(value = AuditingEntityListener.class)
@ApiModel(value = "TFileSystem")
public class FileSystem implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid")
  private String id;

  @ApiModelProperty(value = "文件原始名称")
  private String fileName;

  @ApiModelProperty(value = "文件类型，不包含点")
  private String fileType;

  @ApiModelProperty(value = "fastdfs路径")
  private String filePath;

  @ApiModelProperty(value = "大小，单位字节")
  private Long fileSize;

  @ApiModelProperty(value = "图片宽度")
  private Integer pictureWidth;

  @ApiModelProperty(value = "图片高度")
  private Integer pictureHeight;

  @ApiModelProperty(value = "绑定的用户ID")
  private String userId;

  @ApiModelProperty(value = "业务key")
  private String businessKey;

  @ApiModelProperty(value = "业务标签")
  private String businessTag;

  @ApiModelProperty(value = "元数据")
  private String metaData;

  @ApiModelProperty(value = "组")
  private String groupName;

  @CreatedDate
  @ApiModelProperty(value = "创建时间", hidden = true)
  private Date createTime;

  @LastModifiedDate
  @ApiModelProperty(value = "更新时间", hidden = true)
  private Date updateTime;

}
