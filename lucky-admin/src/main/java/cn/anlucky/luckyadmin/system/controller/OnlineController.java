package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.system.service.OnlineService;
import cn.anlucky.luckyadmin.system.pojo.UserLoginDetail;
import cn.anlucky.luckyadmin.vo.R;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "在线用户Controller")
@RequiredArgsConstructor
@RequestMapping("/monitor/online")
public class OnlineController {

    private final OnlineService onlineService;

    @Operation(summary = "分页查询在线用户")
    @SaCheckPermission("monitor::online::query")
    @PostMapping("/pageByParams")
    public Object pageByParams(@DefaultValue("1") Integer currentPage,
                               @DefaultValue("10") Integer pageSize,
                               @RequestBody UserLoginDetail userLoginDetail)
    {
        return R.ok(onlineService.pageByParams(currentPage,pageSize,userLoginDetail));
    }

    @Operation(summary = "强制下线")
    @SaCheckPermission("monitor::online::kickout")
    @GetMapping("/forceLogout")
    public Object forceLogout(String token) {
        onlineService.kickOut(token);
        return R.ok("强制下线成功");
    }



}
