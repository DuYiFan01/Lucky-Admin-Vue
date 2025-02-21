package cn.anlucky.luckyadmin.system.service;


import cn.anlucky.luckyadmin.system.vo.UserLoginDetail;
import cn.anlucky.luckyadmin.utils.page.vo.PageDataVo;

import java.util.List;

/**
 * 在线用户 服务类
 */
public interface OnlineService {


    /**
     * 分页查询在线用户
     * @param userLoginDetail
     * @return
     */
    public PageDataVo pageByParams(Integer currentPage, Integer pageSize,UserLoginDetail userLoginDetail);

    /**
     * 强制下线
     * @param token
     */
    public void kickOut(String token);




}
