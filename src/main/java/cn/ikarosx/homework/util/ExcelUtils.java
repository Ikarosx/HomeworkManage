package cn.ikarosx.homework.util;

import cn.ikarosx.homework.exception.CommonCodeEnum;
import cn.ikarosx.homework.exception.ExceptionCast;
import cn.ikarosx.homework.model.excel.ExcelAnnotation;
import cn.ikarosx.homework.model.excel.HomeworkFinishInfoExcelModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel 工具类
 *
 * @author mrcoder
 * @version 1.0 2019.05.29
 */
@Slf4j
public class ExcelUtils {

  @Test
  public void test() throws Exception {
//    createExcelTemplateByClass(
//        HomeworkFinishInfoExcelModel.class, "src/main/resources/excel/HomeworkFinishInfoTemplate.xlsx");
  }

  /**
   * 通过类创建Excel模板
   *
   * @param clazz 类
   * @param templatePath 模板名称，包括路径
   */
  public <T> void createExcelTemplateByClass(Class<T> clazz, String templatePath) throws Exception {
    Field[] fields = clazz.getDeclaredFields();
    File file = ResourceUtils.getFile(templatePath);
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet();
    XSSFRow row = sheet.createRow(0);
    int fieldsLength = fields.length;
    for (int i = 0; i < fieldsLength; i++) {
      String name = fields[i].getName();
      char c = name.charAt(0);
      // 首字母大写
      name = name.replaceFirst(String.valueOf(c), String.valueOf((char) (c - 32)));
      row.createCell(i).setCellValue(name);
    }
    for (int i = 0; i < fieldsLength; i++) {
      sheet.autoSizeColumn(i);
    }
    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      workbook.write(outputStream);
    }
  }

  /**
   * 读取excel反射实体
   *
   * @param file MultipartFile
   * @param clazz entity
   */
  public static <T extends Object> List<T> readExcelObject(MultipartFile file, Class<T> clazz) {

    // 存储excel数据
    List<T> list = new ArrayList<>();
    Workbook wookbook = null;
    InputStream inputStream = null;

    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
      throw new RuntimeException("文件读取异常");
    }

    // 根据excel文件版本获取工作簿
    if (file.getOriginalFilename().endsWith(".xls")) {
      wookbook = xls(inputStream);
    } else if (file.getOriginalFilename().endsWith(".xlsx")) {
      wookbook = xlsx(inputStream);
    } else {
      throw new RuntimeException("非excel文件");
    }

    // 得到一个工作表
    Sheet sheet = wookbook.getSheetAt(0);

    // 获取行总数
    int rows = sheet.getLastRowNum() + 1;
    Row row;

    // 获取类所有属性
    Field[] fields = clazz.getDeclaredFields();

    T obj = null;
    int coumnIndex = 0;
    Cell cell = null;
    ExcelAnnotation excelAnnotation = null;
    for (int i = 1; i < rows; i++) {
      // 获取excel行
      row = sheet.getRow(i);
      // 此处用来过滤空行
      Cell cell0 = row.getCell(0);
      cell0.setCellType(CellType.STRING);
      Cell cell1 = row.getCell(1);
      cell1.setCellType(CellType.STRING);
      if (cell0.getStringCellValue() == "" && cell1.getStringCellValue() == "") {
        continue;
      }
      try {
        // 创建实体
        obj = clazz.newInstance();
        for (Field f : fields) {
          // 设置属性可访问
          f.setAccessible(true);
          // 判断是否是注解
          if (f.isAnnotationPresent(ExcelAnnotation.class)) {
            // 获取注解
            excelAnnotation = f.getAnnotation(ExcelAnnotation.class);
            // 获取列索引
            coumnIndex = excelAnnotation.columnIndex();
            // 获取单元格
            cell = row.getCell(coumnIndex);
            // 设置属性
            setFieldValue(obj, f, wookbook, cell);
          }
        }
        System.out.println(obj);
        // 添加到集合中
        list.add(obj);
      } catch (InstantiationException e1) {
        e1.printStackTrace();
      } catch (IllegalAccessException e1) {
        e1.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("excel文件内容出错");
      }
    }
    try {
      // 释放资源
      wookbook.close();
      inputStream.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }

  /**
   * 导出Excel
   *
   * @param response Sevlet响应
   * @param template xlsx模板文件路径，填写classpath:后面部分即可，Excel头中每一列以ExcelTemplate这种形式
   * @param clazz 实体类，需要带上ExcelAnnotation注解以更换Excel的头
   * @param datas 数据
   */
  public static <T> void exportExcel(
      HttpServletResponse response, String template, Class<T> clazz, List<T> datas) {
    try {
      // 1.读取Excel模板
      File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + template);
      InputStream in = new FileInputStream(file);
      XSSFWorkbook wb = new XSSFWorkbook(in);
      // 2.读取模板里第一个Sheet
      XSSFSheet sheet = wb.getSheetAt(0);
      // 3.设置公式自动读取
      sheet.setForceFormulaRecalculation(true);
      XSSFRow headerRow = sheet.getRow(0);
      int cellNums = headerRow.getPhysicalNumberOfCells();
      Method[] methods = new Method[cellNums];

      for (int j = 0; j < methods.length; j++) {
        try {
          methods[j] = clazz.getDeclaredMethod("get" + headerRow.getCell(j).getStringCellValue());
        } catch (NoSuchMethodException e) {
          log.error("在获取{}字段值时获取不到", headerRow.getCell(j).getStringCellValue());
          ExceptionCast.cast(CommonCodeEnum.EXCEL_GET_FIELD_ERROR);
        }
      }
      // 行数
      int i = 1;
      for (T data : datas) {
        // 5.创建新行
        XSSFRow row = sheet.createRow(i++);
        // 遍历每一列设置
        for (int j = 0; j < cellNums; j++) {
          // 反射拿到get值
          Object value = methods[j].invoke(data);
          // 创建列
          XSSFCell cell = row.createCell(j);
          if (value instanceof Date) {
            // 格式化，解决在Excel里格式问题
            cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value));
          } else {
            cell.setCellValue(String.valueOf(value));
          }
        }
      }
      // 处理首行为对应注解所标明的值
      // 遍历每一列设置
      for (int j = 0; j < cellNums; j++) {
        // 反射拿到get值
        XSSFCell cell = headerRow.getCell(j);
        String cellValue = cell.getStringCellValue();
        // 驼峰处理
        char c = cellValue.charAt(0);
        cellValue = cellValue.replaceFirst(String.valueOf(c), String.valueOf((char) (c + 32)));
        // 拿到对应字段名字的field
        Field field = clazz.getDeclaredField(cellValue);
        ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
        if (annotation == null) {
          log.error("在获取{}字段的ExcelAnnotation注解时获取不到", field.getName());
          ExceptionCast.cast(CommonCodeEnum.EXCEL_GET_ANNOTATION_ERROR);
        }
        // 设置行头
        cell.setCellValue(annotation.columnName());
      }
      for (int j = 0; j < cellNums; j++) {
        sheet.autoSizeColumn(i);
      }
      String fileName =
          new String(
              (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())
                      + "_"
                      + clazz.getSimpleName())
                  .getBytes(),
              "UTF-8");
      response.setContentType("multipart/form-data");
      response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
      response.setHeader("Pragma", "No-cache");
      response.setCharacterEncoding("utf-8");
      ServletOutputStream out = response.getOutputStream();
      wb.write(out);
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  /**
   * 绑定实体值
   *
   * @param obj Object
   * @param f Field
   * @param wookbook Workbook
   * @param cell Cell
   */
  private static void setFieldValue(Object obj, Field f, Workbook wookbook, Cell cell) {
    try {

      cell.setCellType(CellType.STRING);

      if (f.getType() == byte.class || f.getType() == Byte.class) {

        f.set(obj, Byte.parseByte(cell.getStringCellValue()));

      } else if (f.getType() == int.class || f.getType() == Integer.class) {

        f.set(obj, Integer.parseInt(cell.getStringCellValue()));

      } else if (f.getType() == Double.class || f.getType() == double.class) {

        f.set(obj, Double.parseDouble(cell.getStringCellValue()));

      } else if (f.getType() == BigDecimal.class) {

        f.set(obj, new BigDecimal(cell.getStringCellValue()));

      } else {

        f.set(obj, cell.getStringCellValue());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 对excel 2003处理 */
  private static Workbook xls(InputStream is) {
    try {
      // 得到工作簿
      return new HSSFWorkbook(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** 对excel 2007处理 */
  private static Workbook xlsx(InputStream is) {
    try {
      // 得到工作簿
      return new XSSFWorkbook(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
