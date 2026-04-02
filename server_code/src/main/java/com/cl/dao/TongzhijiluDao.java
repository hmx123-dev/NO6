package com.cl.dao;

import com.cl.entity.TongzhijiluEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cl.entity.view.TongzhijiluView;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知记录
 * 
 * @author 
 * @email 
 * @date 2025-04-02
 */
public interface TongzhijiluDao extends BaseMapper<TongzhijiluEntity> {
	
	List<TongzhijiluView> selectListView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);

	TongzhijiluView selectView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
	
}
