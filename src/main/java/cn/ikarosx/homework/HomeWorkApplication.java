package cn.ikarosx.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Ikarosx
 * @date 2020/8/15 8:08
 */
@SpringBootApplication
@EnableJpaAuditing
public class HomeWorkApplication {
  public static void main(String[] args) {
    SpringApplication.run(HomeWorkApplication.class, args);
  }
}
