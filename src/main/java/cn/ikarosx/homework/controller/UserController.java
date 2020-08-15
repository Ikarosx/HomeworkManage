package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.aspect.IsOwner;
import cn.ikarosx.homework.aspect.NeedAdmin;
import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.model.param.insert.UserInsertParam;
import cn.ikarosx.homework.model.param.query.UserQueryParam;
import cn.ikarosx.homework.model.param.update.UserUpdateParam;
import cn.ikarosx.homework.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Ikarosx
 * @date 2020/08/15 14:10
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户")
public class UserController {
  @Autowired private UserService userService;

  @PostMapping
  @ApiOperation(value = "新增用户")
  public ResponseResult insertUser(@Validated @RequestBody UserInsertParam userInsertParam) {
    userInsertParam.setPassword(BCrypt.hashpw(userInsertParam.getPassword(), BCrypt.gensalt()));
    return userService.insertUser(userInsertParam);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "通过ID删除用户")
  @IsOwner
  public ResponseResult deleteUserById(@PathVariable String id) {
    return userService.deleteUserById(id);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "更新用户")
  @IsOwner(position = 1, clazz = UserUpdateParam.class)
  public ResponseResult updateUser(
      @PathVariable String id, @Validated @RequestBody UserUpdateParam userUpdateParam) {
    userUpdateParam.setPassword(BCrypt.hashpw(userUpdateParam.getPassword(), BCrypt.gensalt()));
    return userService.updateUser(userUpdateParam);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "通过ID查询用户")
  @IsOwner
  public ResponseResult getUserById(@PathVariable String id) {
    return userService.getUserById(id);
  }

  @GetMapping("/{page}/{size}")
  @ApiOperation(value = "分页查询用户")
  public ResponseResult listUsersByPage(
      @PathVariable int page, @PathVariable int size, UserQueryParam userQueryParam) {
    if (page < 1) {
      page = 1;
    }
    if (size <= 0) {
      size = 10;
    }
    if (userQueryParam == null) {
      userQueryParam = new UserQueryParam();
    }
    return userService.listUsersByPage(page, size, userQueryParam);
  }

  @GetMapping
  @ApiOperation(value = "查询全部用户")
  @NeedAdmin
  public ResponseResult listAllUsers() {
    return userService.listAllUsers();
  }

  @PostMapping("/login")
  @ApiOperation("用户登录")
  public ResponseResult login(@RequestParam String username, @RequestParam String password) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      return CommonCodeEnum.INVALID_PARAM;
    }
    return userService.login(username, password);
  }

  @GetMapping("/logout")
  @ApiOperation("用户注销")
  public ResponseResult logout() {
    return userService.logout();
  }
}
