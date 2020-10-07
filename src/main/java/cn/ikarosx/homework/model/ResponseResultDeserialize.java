package cn.ikarosx.homework.model;

import cn.ikarosx.homework.exception.ResponseResult;
import cn.ikarosx.homework.exception.ResponseResultImpl;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import java.io.IOException;
import java.util.Map;

/**
 * 处理ResponseResult反序列化
 *
 * @author Ikarosx
 * @date 2020/9/18 9:12
 */
public class ResponseResultDeserialize extends JsonDeserializer<ResponseResult> {

  @Override
  public ResponseResult deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = jp.getCodec().readTree(jp);
    boolean success = node.get("success").booleanValue();
    String message = node.get("message").asText();
    int code = (int) node.get("code").numberValue();
    JsonNode data = node.get("data");
    ObjectMapper mapper = new ObjectMapper();
    // 不抛出异常
    mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
    Map result = mapper.convertValue(data, Map.class);
    if (result == null) {
      mapper.activateDefaultTyping(
          LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
      result = mapper.convertValue(data, Map.class);
    }
    ResponseResultImpl responseResult = new ResponseResultImpl(success, code, message);
    responseResult.setData(result);
    return responseResult;
  }
}
