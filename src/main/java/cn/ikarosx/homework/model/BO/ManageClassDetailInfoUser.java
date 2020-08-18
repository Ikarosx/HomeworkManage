package cn.ikarosx.homework.model.BO;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020-08-15 01:42:01
 */
@Data
@AllArgsConstructor
public class ManageClassDetailInfoUser implements Serializable {
  private String id;
  private String username;
  private String nickname;
  private String studentNo;
  private Date createTime;
}
