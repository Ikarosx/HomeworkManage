package cn.ikarosx.homework.model.BO;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/8/16 12:56
 */
@Data
public class ManageClassDetailInfo {
  private String id;
  // 班级名称
  private String name;
  // 班级人数
  private Integer num;
  // 管理员信息
  private ManageClassDetailInfoUser adminUser;
  private Date createTime;
  private Date updateTime;
  // 班级同学
  private List<ManageClassDetailInfoUser> members;
}
