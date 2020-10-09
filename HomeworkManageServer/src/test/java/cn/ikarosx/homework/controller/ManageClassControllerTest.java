package cn.ikarosx.homework.controller;

import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ResponseResult;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * @author Ikarosx
 * @date 2020/10/4 13:54
 */
// @SpringBootTest
// @RunWith(SpringRunner.class)
class ManageClassControllerTest {

  @Test
  void getManageClassDetailInfo() {
    // 重点
    ObjectMapper om = new ObjectMapper();
    // 访问控制权限
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 反序列化时候遇到不匹配的属性并不抛出异常
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 序列化时候遇到空对象不抛出异常
    om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    // 反序列化的时候如果是无效子类型,不抛出异常
    //    om.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
    // 不使用默认的dateTime进行序列化,
    om.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    // 使用JSR310提供的序列化类,里面包含了大量的JDK8时间序列化类
    om.registerModule(new JavaTimeModule());
    // 启用反序列化所需的类型信息,在属性中添加@class
    om.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, As.PROPERTY);

    String json =
        "{\"@class\":\"cn.ikarosx.homework.exception.CommonCodeEnum\",\"success\":true,\"code\":10000,\"message\":\"操作成功！\",\"data\":{\"@class\":\"java.util.HashMap\",\"manageClassDetailInfo\":{\"@class\":\"cn.ikarosx.homework.model.BO.ManageClassDetailInfo\",\"id\":\"8a80cb81746161f201746169957d0000\",\"name\":\"17软件工程B班\",\"num\":2,\"adminUser\":{\"@class\":\"cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser\",\"id\":\"8a80cb8174612bb301746132cd5d0001\",\"username\":\"Ikarosx\",\"nickname\":\"许培宇\",\"studentNo\":\"17551119111\",\"createTime\":[\"java.sql.Timestamp\",1599362144000]},\"createTime\":[\"java.sql.Timestamp\",1599362143609],\"updateTime\":[\"java.sql.Timestamp\",1599362143609],\"members\":[\"java.util.ArrayList\",[{\"@class\":\"cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser\",\"id\":\"8a80cb8174612bb301746132cd5d0001\",\"username\":\"Ikarosx\",\"nickname\":\"许培宇\",\"studentNo\":\"17551119111\",\"createTime\":[\"java.sql.Timestamp\",1599362144000]},{\"@class\":\"cn.ikarosx.homework.model.BO.ManageClassDetailInfoUser\",\"id\":\"8a80cb81746161f20174616a85510002\",\"username\":\"HQZ\",\"nickname\":\"黄启壮\",\"studentNo\":\"17551119080\",\"createTime\":[\"java.sql.Timestamp\",1599362598000]}]]}}}";
    //    String json =
    //
    // "{\"success\":true,\"code\":10000,\"message\":\"操作成功！\",\"data\":{\"manageClassDetailInfo\":{\"id\":\"8a80cb81746161f201746169957d0000\",\"name\":\"17软件工程B班\",\"num\":2,\"adminUser\":{\"id\":\"8a80cb8174612bb301746132cd5d0001\",\"username\":\"Ikarosx\",\"nickname\":\"许培宇\",\"studentNo\":\"17551119111\",\"createTime\":1599362144000},\"createTime\":1599362143609,\"updateTime\":1599362143609,\"members\":[{\"id\":\"8a80cb8174612bb301746132cd5d0001\",\"username\":\"Ikarosx\",\"nickname\":\"许培宇\",\"studentNo\":\"17551119111\",\"createTime\":1599362144000},{\"id\":\"8a80cb81746161f20174616a85510002\",\"username\":\"HQZ\",\"nickname\":\"黄启壮\",\"studentNo\":\"17551119080\",\"createTime\":1599362598000}]}}}";
    //    om.activateDefaultTyping(om.getPolymorphicTypeValidator(), DefaultTyping.NON_FINAL);
    try {
//      json = om.writeValueAsString(CommonCodeEnum.SUCCESS.addData("date", new Date()));
      System.out.println(json);
      ResponseResult responseResult = om.readValue(json, ResponseResult.class);
      System.out.println(responseResult);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    //    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);

    //    om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

  }
}
