package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.annotation.SysLog;

import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.view.TongzhijiluView;

import com.cl.service.TongzhijiluService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 通知记录
 * 后端接口
 * @author 
 * @email 
 * @date 2025-04-02
 */
@RestController
@RequestMapping("/tongzhijilu")
public class TongzhijiluController {
    @Autowired
    private TongzhijiluService tongzhijiluService;

    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TongzhijiluEntity tongzhijilu,
                                                                                                                                                                                                                                HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
                                                                                                                                                                                                                                                                    if(tableName.equals("yonghu")) {
            tongzhijilu.setZhanghao((String)request.getSession().getAttribute("username"));
        }
                                                                                                                                                                        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
                                                                                                                                                                                                                                
        
        PageUtils page = tongzhijiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhijilu), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TongzhijiluEntity tongzhijilu,
		HttpServletRequest request){
        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();

		PageUtils page = tongzhijiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhijilu), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( TongzhijiluEntity tongzhijilu){
       	EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tongzhijilu, "tongzhijilu")); 
        return R.ok().put("data", tongzhijiluService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(TongzhijiluEntity tongzhijilu){
        EntityWrapper< TongzhijiluEntity> ew = new EntityWrapper< TongzhijiluEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tongzhijilu, "tongzhijilu")); 
		TongzhijiluView tongzhijiluView =  tongzhijiluService.selectView(ew);
		return R.ok("查询通知记录成功").put("data", tongzhijiluView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TongzhijiluEntity tongzhijilu = tongzhijiluService.selectById(id);
		tongzhijilu = tongzhijiluService.selectView(new EntityWrapper<TongzhijiluEntity>().eq("id", id));
        return R.ok().put("data", tongzhijilu);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TongzhijiluEntity tongzhijilu = tongzhijiluService.selectById(id);
		tongzhijilu = tongzhijiluService.selectView(new EntityWrapper<TongzhijiluEntity>().eq("id", id));
        return R.ok().put("data", tongzhijilu);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增通知记录")
    public R save(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
    	tongzhijiluService.insert(tongzhijilu);
        return R.ok();
    }
    
    @SysLog("新增通知记录")
    @RequestMapping("/add")
    public R add(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
    	tongzhijiluService.insert(tongzhijilu);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改通知记录")
    public R update(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        tongzhijiluService.updateById(tongzhijilu);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除通知记录")
    public R delete(@RequestBody Long[] ids){
        tongzhijiluService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 重试发送通知
     */
    @RequestMapping("/retry/{id}")
    @SysLog("重试发送通知")
    public R retry(@PathVariable("id") Long id){
        boolean success = tongzhijiluService.retryNotification(id);
        if(success) {
            return R.ok("重试发送成功");
        } else {
            return R.error("重试发送失败");
        }
    }
    
    /**
     * 批量重试发送
     */
    @RequestMapping("/retryBatch")
    @Transactional
    @SysLog("批量重试发送通知")
    public R retryBatch(@RequestBody Long[] ids){
        int successCount = 0;
        int failCount = 0;
        for(Long id : ids) {
            boolean success = tongzhijiluService.retryNotification(id);
            if(success) {
                successCount++;
            } else {
                failCount++;
            }
        }
        return R.ok("重试完成：成功" + successCount + "条，失败" + failCount + "条");
    }
    
    /**
     * 更新接收状态
     */
    @RequestMapping("/updateReceiveStatus")
    public R updateReceiveStatus(@RequestParam Long id, @RequestParam Integer status){
        tongzhijiluService.updateReceiveStatus(id, status);
        return R.ok("更新接收状态成功");
    }
    
    /**
     * 获取需要重试的通知列表
     */
    @RequestMapping("/retryList")
    public R retryList(){
        List<TongzhijiluEntity> list = tongzhijiluService.getRetryNotifications();
        return R.ok().put("data", list);
    }
    
    /**
     * 统计通知状态
     */
    @RequestMapping("/statistics")
    public R statistics(){
        // 统计各状态数量
        EntityWrapper<TongzhijiluEntity> wrapper0 = new EntityWrapper<>();
        wrapper0.eq("fasongzhuangtai", 0);
        int pendingCount = tongzhijiluService.selectCount(wrapper0);
        
        EntityWrapper<TongzhijiluEntity> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("fasongzhuangtai", 1);
        int successCount = tongzhijiluService.selectCount(wrapper1);
        
        EntityWrapper<TongzhijiluEntity> wrapper2 = new EntityWrapper<>();
        wrapper2.eq("fasongzhuangtai", 2);
        int failCount = tongzhijiluService.selectCount(wrapper2);
        
        Map<String, Object> result = new HashMap<>();
        result.put("pendingCount", pendingCount);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", pendingCount + successCount + failCount);
        
        return R.ok().put("data", result);
    }
}
