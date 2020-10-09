package cn.ikarosx.homework.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * SpringCache-Redis配置
 *
 * @author 许培宇
 * @date 2020/10/04 22:55
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

  /** 不重写的话 ，采用默认策略 ①如果方法没有参数，则使用0作为key ②如果只有一个参数的话则使用该参数作为key。 ③如果参数多于一个的话则使用所有参数的hashCode作为key */
  @Override
  @Bean
  public KeyGenerator keyGenerator() {
    // 当没有指定缓存的 key时来根据类名、方法名和方法参数来生成key
    return (target, method, params) -> {
      StringBuilder sb = new StringBuilder();
      sb.append(target.getClass().getName()).append(':').append(method.getName());
      if (params.length > 0) {
        sb.append('[');
        for (Object obj : params) {
          if (obj != null) {
            sb.append(obj.toString());
          }
        }
        sb.append(']');
      }
      System.out.println("keyGenerator=" + sb.toString());
      return sb.toString();
    };
  }

  /**
   * 申明缓存管理器，会创建一个切面（aspect）并触发Spring缓存注解的切点（pointcut）
   * 根据类或者方法所使用的注解以及缓存的状态，这个切面会从缓存中获取数据，将数据添加到缓存之中或者从缓存中移除某个值
   */
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        // 让Cache管理器使用我们的配置文件，因为需要自定义序列化
        .cacheDefaults(redisCacheConfiguration())
        .build();
  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {

    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(Object.class);
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
        LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
    jackson2JsonRedisSerializer.setObjectMapper(om);

    // 构建配置文件
    RedisCacheConfiguration configuration =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    jackson2JsonRedisSerializer))
            .entryTtl(Duration.ofMinutes(30));

    return configuration;
  }
}
