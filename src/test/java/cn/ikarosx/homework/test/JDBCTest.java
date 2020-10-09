package cn.ikarosx.homework.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ikarosx
 * @date 2020/10/3 17:01
 */
public class JDBCTest {

  public static final String USER = "homework";
  public static final String PASSWORD = "newLifeHomeWork";
  public static final String URL =
      "jdbc:mysql://ikarosx.cn:3307/homework?useSSL=false&CharacterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
  Connection connection;

  @Before
  public void sqlInjectBefore() throws Exception {
    System.out.println("获取数据库连接");
    Class.forName("com.mysql.cj.jdbc.Driver");
    connection = DriverManager.getConnection(URL, USER, PASSWORD);
    System.out.println("获取数据库连接成功");
  }

  @Test
  public void sqlInjectTest() throws Exception {
    String id = "8a80cb8174619da6017461a2ec2d0002";
    String title = "213333";

    String sql = "SELECT * FROM manage_homework WHERE id = ? AND title = ?";
    System.out.println("执行sql语句" + sql);
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, id);
    statement.setString(2, title);
    ResultSet resultSet = statement.executeQuery();
    System.out.println("语句执行成功");
    while (resultSet.next()) {
      System.out.println(resultSet.getString("id"));
    }
  }

  @After
  public void sqlInjectAfter() throws Exception {
    System.out.println("关闭数据库连接");
    connection.close();
    System.out.println("关闭数据库连接成功");
  }
}
