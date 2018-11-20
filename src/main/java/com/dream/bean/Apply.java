package com.dream.bean;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author weibo
 * @since 2018-11-19
 */
@TableName("t_apply")
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String APPLY_STATUS_ING = "审批中";
    public static final String APPLY_STATUS_SUCCESS = "通过审批";
    public static final String APPLY_STATUS_REFUSE = "未通过";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String startdate;

    private String enddate;

    private Integer days;
    private String type;
    private Integer excuteId;
    private Integer applyId;
    private String status;
    private String remark;
    private String  applydate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getExcuteId() {
        return excuteId;
    }

    public void setExcuteId(Integer excuteId) {
        this.excuteId = excuteId;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplydate() {
        return applydate;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
    }

    @Override
    public String toString() {
        return "Apply{" +
                ", id=" + id +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", days=" + days +
                ", type=" + type +
                ", excuteId=" + excuteId +
                ", applyId=" + applyId +
                ", status=" + status +
                ", remark=" + remark +
                ", applydate=" + applydate +
                "}";
    }
}
