package com.dream.bean;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
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
 * @since 2018-11-09
 */
@TableName("t_loginlog")
public class Loginlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String ip;
    private String no;
    private String createtime;
    private String location;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Loginlog() {
    }

    public Loginlog(String ip, String no, String createtime, String location) {
        this.ip = ip;
        this.no = no;
        this.createtime = createtime;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Loginlog{" +
        ", id=" + id +
        ", ip=" + ip +
        ", no=" + no +
        ", createtime=" + createtime +
        ", location=" + location +
        "}";
    }
}
