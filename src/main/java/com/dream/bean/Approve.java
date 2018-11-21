package com.dream.bean;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dragon
 * @since 2018-11-21
 */
@TableName("t_approve")
public class Approve implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @TableField("approveDate")
    private String approveDate;
    private String comment;
    private Integer applyId;
    /**
     * 申请是否通过  1   0
     */
    @TableField("approveValue")
    private Boolean approveValue;

    @TableField(exist = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Boolean getApproveValue() {
        return approveValue;
    }

    public void setApproveValue(Boolean approveValue) {
        this.approveValue = approveValue;
    }

    @Override
    public String toString() {
        return "Approve{" +
        ", id=" + id +
        ", userId=" + userId +
        ", approveDate=" + approveDate +
        ", comment=" + comment +
        ", applyId=" + applyId +
        ", approveValue=" + approveValue +
        "}";
    }
}
