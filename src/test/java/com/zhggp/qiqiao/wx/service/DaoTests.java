package com.zhggp.qiqiao.wx.service;

import com.zhggp.qiqiao.wx.BootApplication;
import com.zhggp.qiqiao.wx.db.dao.WxDataCubeUserMapper;
import com.zhggp.qiqiao.wx.db.entity.WxDataCubeUserEntity;
import com.zhggp.qiqiao.wx.service.impl.WxUserAnalyzeServiceImpl;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
public class DaoTests {

    @Autowired
    WxUserAnalyzeServiceImpl wxUserAnalyzeService;

    @Test
    public void hi() {
        System.out.println("hello world");
    }

//    @Test
//    public void insert() {
//        WxDataCubeUserEntity entity = new WxDataCubeUserEntity();
//        entity.setRefDate(new Date(120, 11, 29));
//        wxDataCubeUserMapper.insert(entity);
//    }

    @Test
    public void insertBatch() {
        List<WxDataCubeUserEntity> list = new ArrayList<>();
        for (int i = 20; i < 30; i++) {
            WxDataCubeUserEntity entity1 = new WxDataCubeUserEntity();
            entity1.setRefDate(new Date(120,11,i));
            list.add(entity1);
        }

        wxUserAnalyzeService.saveOrUpdateBatch(list);
    }
}