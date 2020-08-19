package cn.ikarosx.homework.model.param.update;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ikarosx
 * @date 2020/08/17 20:29
 */
@Data
@ApiModel(value = "作业提交记录Update参数")
public class ManageHomeworkUserUpdateParam {
  @NotNull private String content;
  @NotNull private String id;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
