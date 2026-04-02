package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.TongzhijiluEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.TongzhijiluView;


/**
 * 通知记录
 *
 * @author 
 * @email 
 * @date 2025-04-02
 */
public interface TongzhijiluService extends IService<TongzhijiluEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<TongzhijiluView> selectListView(Wrapper<TongzhijiluEntity> wrapper);
   	
   	TongzhijiluView selectView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<TongzhijiluEntity> wrapper);
   	
   	/**
   	 * 创建预约通知记录
   	 * @param yuyuebianhao 预约编号
   	 * @param yishengzhanghao 医生账号
   	 * @param zhanghao 用户账号
   	 * @param jiuzhenshijian 就诊时间
   	 */
   	void createAppointmentNotifications(String yuyuebianhao, String yishengzhanghao, String zhanghao, java.util.Date jiuzhenshijian);
   	
   	/**
   	 * 发送通知
   	 * @param tongzhijilu 通知记录
   	 * @return 是否发送成功
   	 */
   	boolean sendNotification(TongzhijiluEntity tongzhijilu);
   	
   	/**
   	 * 重试发送失败的通知
   	 * @param id 通知记录ID
   	 * @return 是否重试成功
   	 */
   	boolean retryNotification(Long id);
   	
   	/**
   	 * 更新用户接收状态
   	 * @param id 通知记录ID
   	 * @param status 接收状态
   	 */
   	void updateReceiveStatus(Long id, Integer status);
   	
   	/**
   	 * 获取需要重试的通知列表
   	 * @return 通知列表
   	 */
   	List<TongzhijiluEntity> getRetryNotifications();
}
