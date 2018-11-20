package com.dream.utils;

import com.dream.bean.Student;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    public static List<Student> readFromExcel(String filePath) {
        File excelFile = null;// Excel文件对象
        InputStream is = null;// 输入流对象
        String cellStr = null;// 单元格，最终按字符串处理

        List<Student> studentList = new ArrayList<Student>();// 返回封装数据的List 学生集合
        // 填充数据
        Student student = null;// 每一个学生信息对象

        try {
            excelFile = new File(filePath);
            is = new FileInputStream(excelFile);// 获取文件输入流

            HSSFWorkbook workbook = new HSSFWorkbook(is);// 创建Excel文件对象
            HSSFSheet sheet = workbook.getSheetAt(0);// 取出第一个工作表，索引是0

            // 开始循环遍历行，表头不处理，从1开始
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                student = new Student();// 实例化Student对象
                HSSFRow row = sheet.getRow(i);// 获取行对象
                if (row == null) {// 如果为空，不处理
                    continue;
                }
                // 循环遍历单元格
                for (int j = 0; j < row.getLastCellNum(); j++) {

                    HSSFCell cell = row.getCell(j);// 获取单元格对象
                    if (cell == null) {// 单元格为空设置cellStr为空串
                        cellStr = "";
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {// 对布尔值的处理
                        cellStr = String.valueOf(cell.getBooleanCellValue());
                    } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {// 对数字值的处理
                        cellStr = cell.getNumericCellValue() + "";
                    } else {// 其余按照字符串处理
                        cellStr = cell.getStringCellValue();
                    }

                    // 下面按照数据出现位置封装到bean中
                    if (j == 0) {
                        student.setNo(cellStr);
                    } else if (j == 1) {
                        student.setName(cellStr);
                    } else if (j == 2) {
                        student.setSex(cellStr);
                    } else if (j == 3) {
                        student.setBirthday(cellStr);
                    } else if (j == 4) {
                        student.setCardno(cellStr);
                    } else if (j == 5) {
                        student.setSchool(cellStr);
                    } else if (j == 6) {
                        student.setEducation(cellStr);
                    } else if (j == 7) {
                        student.setEmail(cellStr);
                    } else if (j == 8) {
                        student.setQq(cellStr);
                    } else if (j == 9) {
                        student.setPhone(cellStr);
                    } else if (j == 10) {
                        student.setCreatedate(cellStr);
                    }
                }
                studentList.add(student);// 数据装入List
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {// 关闭文件流
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return studentList;
    }
}
