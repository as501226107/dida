package com.dream.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dream.bean.Course;
import com.dream.bean.Grade;
import com.dream.bean.Student;
import com.dream.service.GradeService;
import com.dream.service.StudentService;
import com.dream.utils.CreateFileUtils;
import com.dream.utils.ExcelUtils;
import com.dream.utils.FileUtils;
import com.dream.utils.PageHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dragon
 * @since 2018-11-08
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentService ss;
    @Autowired
    GradeService gs;

    @RequestMapping("/list/{pageIndex}")
    public String lists(@PathVariable("pageIndex") Integer pageIndex, @RequestParam(defaultValue = "5") Integer pageSize, Model model,Student student){
        Page<Student> page = new Page<>(pageIndex, pageSize);
        Student s1=new Student();
        //如果有gid就更新，没有就不更新
        if(student.getGid()!=null&&student.getGid()!=0){
            s1.setGid(student.getGid());
        }
        Page<Student> pages = ss.selectPage(page, new EntityWrapper<Student>(s1).eq("del", "0").like("name",student.getName()));
        //查询出的数据
        List<Student> records = pages.getRecords();
        //加载数据
        int totalCount=((Long)pages.getTotal()).intValue();
        for (Student s:records) {
            Integer gid = s.getGid();
            Grade grade = gs.selectById(gid);
            s.setGrade(grade);
        }
        boolean hasNext = pages.hasNext();
        boolean hasPre = pages.hasPrevious();
        PageHelper<Student> pageBean = new PageHelper<>(pageIndex, pageSize, totalCount, records, student);
        model.addAttribute("pageBean",pageBean);
        model.addAttribute("hasPre",hasPre);
        model.addAttribute("hasNext",hasNext);
        return "/studentlist.jsp";
    }
    @RequestMapping("/pageToUpdate/{id}")
    public String pageToUpdate(@PathVariable("id") Integer id,Model model){
        Student course = ss.selectById(id);
        model.addAttribute("student",course);
        return "/studentupdate.jsp";
    }
    @RequestMapping("/update")
    public void update(Student student, HttpServletResponse response, HttpServletRequest request) throws Exception{
        boolean b = ss.updateById(student);
        if(b){
            response.getWriter().write("<script>alert('更新成功！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('更新失败！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
        }
    }
    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws Exception{
        System.out.println("delete..............");
              /*  Course course = new Course();
                course.setId(id);
                course.setDel(1);*/
            // boolean b = cs.updateById(course);
            boolean b = ss.updateForSet("del=" + 1, new EntityWrapper<Student>().eq("id", id));
            if(b){
                response.getWriter().write("<script>alert('删除成功！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
            }else{
                response.getWriter().write("<script>alert('删除失败！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
            }

    }

    @RequestMapping("/goAdd")
    public String add()
    {
        return "/studentadd.jsp";
    }
    @RequestMapping("/goImport")
    public String goImport()
    {
        return "/studentimport.jsp";
    }

    @RequestMapping("/add")
    public void add(Student student, HttpServletResponse response, HttpServletRequest request) throws Exception{
        //添加学生的同时在向User表中添加登录登录信息，并配置学生角色
        student.setDel(0);
        //初始化学号
        student.setNo(UUID.randomUUID().toString().replace("-", ""));
        boolean b = ss.addStudent(student);
        if(b){
            response.getWriter().write("<script>alert('添加成功！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('添加失败！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
        }
    }
    @RequestMapping("/pageToDeatail/{id}")
    public String pageToDeatail(@PathVariable("id") Integer id,Model model) throws Exception{
        Student student = ss.selectById(id);
        Integer gid = student.getGid();
        Grade grade = gs.selectById(gid);
        student.setGrade(grade);
        model.addAttribute("student",student);
        return "/studentdetails.jsp";
    }
    @RequestMapping("/pageToImport")
    public String pageToImport(Model model){
        List<Grade> grades = gs.selectList(null);
        model.addAttribute("grades",grades);
        return "/studentimport.jsp";
    }
    @RequestMapping("/importExcel")
    public void importExcel(Model model, Integer gid, @RequestParam("mFile") MultipartFile file, HttpServletResponse response, HttpServletRequest request) throws Exception{
        String path="";
        if(!file.isEmpty()){
//            File dir = FileUtils.createDirByUsername("upload", "excel");
//            String name = FileUtils.createName(mFile.getOriginalFilename());
//            File file=new File(dir,name);
//            mFile.transferTo(file);
            // 保存目录
            File head = CreateFileUtils.createDirModel(request.getServletContext(), "excel");
            // 获取文件名
            String filename = file.getOriginalFilename();
            // 生成随机名
            String createName = CreateFileUtils.createName(filename);
            File f = new File(head, createName);
            // 生成保存路径
            path = "media/file/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/" + createName;
            file.transferTo(f);
        }
        List<Student> students = ExcelUtils.readFromExcel(path); // 学生集合
        for (Student student : students) {
            student.setGid(gid);
            student.setDel(0);
        }
        boolean b = ss.insertBatch(students);
        if(b){
            response.getWriter().write("<script>alert('添加成功！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
        }else{
            response.getWriter().write("<script>alert('添加失败！！');location.href='"+request.getContextPath()+"/student/list/1'</script>");
        }
    }
    @RequestMapping("/exportExcel")
    public void exportExcel(Model model, Integer gid, String name, HttpServletResponse response, HttpServletRequest request) throws Exception{
        EntityWrapper<Student> wrapper = new EntityWrapper<>();
        if(name!=null&&!"".equals(name)){
            wrapper.like("name",name);
        }
        if(gid!=null&&gid!=-1){
            wrapper.eq("gid",gid);
        }
        wrapper.eq("del", 0);

        List<Student> students = ss.selectList(wrapper); // 要导出的数据

        for (Student stu : students) {
            System.out.println(stu);
        }
        // 生成Excel表格 作为流返回(下载)

        // 先创建工作簿对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建工作表对象并命名
        HSSFSheet sheet = wb.createSheet("学生信息表");

        int rowCount = 0; // 行数 默认第一行
        sheet.addMergedRegion( new CellRangeAddress(0, 0, 0, 10)  ); //合并单元格
        HSSFRow rowTitle = sheet.createRow(rowCount++);
        HSSFCell cellTile = rowTitle.createCell(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor((short) 13);// 设置背景色
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
        cellTile.setCellStyle(cellStyle);

        cellTile.setCellValue("学生信息表");

        HSSFRow rowHead = sheet.createRow(rowCount++);  //标题行
        String[] titiles={"学号","姓名","性别","出生日期","身份证号","毕业学校","学历","邮箱","QQ","电话","入学日期"};
        for (int i = 0; i < titiles.length; i++) {
            rowHead.createCell(i).setCellValue(titiles[i]);
        }

        // 遍历集合对象创建行和单元格
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);// 取出Student对象
            // 创建行
            HSSFRow row = sheet.createRow(rowCount++);
            // 开始创建单元格并赋值
            HSSFCell noCell = row.createCell(0); // 创建第一个单元格
            noCell.setCellValue(student.getNo());

            HSSFCell nameCell = row.createCell(1);
            nameCell.setCellValue(student.getName());

            HSSFCell sexCell = row.createCell(2);
            sexCell.setCellValue(student.getSex());

            HSSFCell birthday = row.createCell(3);
            birthday.setCellValue((student.getBirthday()));

            HSSFCell cardnoCell = row.createCell(4);
            cardnoCell.setCellValue(student.getCardno());

            HSSFCell schoolCell = row.createCell(5);
            schoolCell.setCellValue(student.getSchool());

            HSSFCell educationCell = row.createCell(6);
            educationCell.setCellValue(student.getEducation());

            HSSFCell emailCell = row.createCell(7);
            emailCell.setCellValue(student.getEmail());

            HSSFCell qqCell = row.createCell(8);
            qqCell.setCellValue(student.getQq());

            HSSFCell phoneCell = row.createCell(9);
            phoneCell.setCellValue(student.getPhone());

            HSSFCell dateCell = row.createCell(10);
            dateCell.setCellValue(student.getCreatedate());
        }
        String fileName="学生信息表"+System.currentTimeMillis()+".xls";//文件名
        //生成Excel并提供下载
        String userAgent=request.getHeader("User-Agent");
        if(userAgent.contains("Safari")){
            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8")) ;
        }else{
            response.addHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"ISO-8859-1")) ;
        }
        ServletOutputStream out = response.getOutputStream(); //输出流
        wb.write(out);
        out.close();//关闭流
    }
    @Test
    public void tgest(){
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }
}

