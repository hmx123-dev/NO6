package com.cl.entity.view;

import com.cl.entity.TongzhijiluEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 通知记录
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 */
@TableName("tongzhijilu")
public class TongzhijiluView extends TongzhijiluEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public TongzhijiluView(){
	}

	public TongzhijiluView(TongzhijiluEntity tongzhijiluEntity){
		try {
			BeanUtils.copyProperties(this, tongzhijiluEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通知类型名称
	 */
	private String tongzhileixingName;

	/**
	 * 发送状态名称
	 */
	private String fasongzhuangtaiName;

	/**
	 * 接收状态名称
	 */
	private String jieshouzhuangtaiName;

	public String getTongzhileixingName() {
		if (tongzhileixing == null) return "";
		switch (tongzhileixing) {
			case 1: return "预约成功通知";
			case 2: return "就诊前一天提醒";
			case 3: return "就诊当天提醒";
			case 4: return "就诊后随访提醒";
			default: return "未知类型";
		}
	}

	public String getFasongzhuangtaiName() {
		if (fasongzhuangtai == null) return "";
		switch (fasongzhuangtai) {
			case 0: return "待发送";
			case 1: return "发送成功";
			case 2: return "发送失败";
			default: return "未知状态";
		}
	}

	public String getJieshouzhuangtaiName() {
		if (jieshouzhuangtai == null) return "";
		switch (jieshouzhuangtai) {
			case 0: return "未接收";
			case 1: return "已接收";
			case 2: return "已读";
			default: return "未知状态";
		}
	}
}
