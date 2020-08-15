
package cn.ikarosx.homework.model.param.update;

import javax.validation.constraints.NotNull;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@Data
@ApiModel(value = "用户Update参数")
public class UserUpdateParam {

    @NotNull private String id;
    @NotNull private String password;
}

    