package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.system.service.SysMenusService;
import cn.anlucky.luckyadmin.system.vo.RouterVo;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SysMenusServiceImpTest {

    @Autowired
    private SysMenusService sysMenusService;

    @Test
    void getAppRouters() {
        List<Long> roles = new ArrayList<>();
        roles.add(1L);

        List<RouterVo> appRouters = sysMenusService.getAppRouters(roles);
        System.out.println("appRouters = " + appRouters);
        String jsonString = JSON.toJSONString(appRouters);
        System.out.println("jsonString = " + jsonString);
    }
}
