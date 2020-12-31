package com.zhggp.qiqiao.wx.service;

import com.zhggp.qiqiao.wx.BootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootApplication.class)
public class DaoTests {

    @Test
    public void hi() {
        System.out.println("hello world");
    }
}