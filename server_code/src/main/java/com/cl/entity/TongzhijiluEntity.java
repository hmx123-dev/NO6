package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("tongzhijilu")
public class TongzhijiluEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public TongzhijiluEntity() {
		
	}

	@TableId(type = IdType.AUTO)
	private Long id;
	private String tongzhibianhao;
	private String zhanghao;
	private String shouji;
	private String tongzhineirong;
	private String songzhuangtai;
	private Integer chongshicishu;
	private Date chongshishijian;
	private String cuowuxinxi;

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getTongzhibianhao() {
		return tongzhibianhao;
	}
	public void setTongzhibianhao(String tongzhibianhao) {
		this.tongzhibianhao = tongzhibianhao;
	}
	public String getZhanghao() {
		return zhanghao;
	}
	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	public String getShouji() {
		return shouji;
	}
	public void setShouji(String shouji) {
		this.shouji = shouji;
	}
	public String getTongzhineirong() {
		return tongzhineirong;
	}
	public void setTongzhineirong(String tongzhineirong) {
		this.tongzhineirong = tongzhineirong;
	}
	public String getSongzhuangtai() {
		return songzhuangtai;
	}
	public void setSongzhuangtai(String songzhuangtai) {
		this.songzhuangtai = songzhuangtai;
	}
	public Integer getChongshicishu() {
		return chongshicishu;
	}
	public void setChongshicishu(Integer chongshicishu) {
		this.chongshicishu = chongshicishu;
	}
	public Date getChongshishijian() {
		return chongshishijian;
	}
	public void setChongshishijian(Date chongshishijian) {
		this.chongshishijian = chongshishijian;
	}
	public String getCuowuxinxi() {
		return cuowuxinxi;
	}
	public void setCuowuxinxi(String cuowuxinxi) {
		this.cuowuxinxi = cuowuxinxi;
	}
}
