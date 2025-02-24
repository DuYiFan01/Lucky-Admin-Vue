package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.system.service.OnlineService;
import cn.anlucky.luckyadmin.system.pojo.UserLoginDetail;
import cn.anlucky.luckyadmin.utils.ListUtils;
import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.anlucky.luckyadmin.utils.page.vo.PageDataVo;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * 在线用户服务实现类
 */
@Service
public class OnlineServiceImpl implements OnlineService {


    /**
     * 分页查询在线用户
     *
     * @param currentPage
     * @param pageSize
     * @param userLoginDetail
     * @return
     */
    @Override
    public PageDataVo pageByParams(Integer currentPage, Integer pageSize, UserLoginDetail userLoginDetail) {
        // todo:这里实现了对username搜索查询分页 没有实现对更多字段的支持
        if (currentPage == null || currentPage < 1) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        List<String> listAll = SaUtils.searchAllTokenValue(false);
        List<UserLoginDetail> userLoginDetailList = new ArrayList<>();
        PageDataVo pageDataVo = new PageDataVo();
        pageDataVo.setCurrentPage(currentPage);
        pageDataVo.setPageSize(pageSize);
        if (StringUtils.isBlank(userLoginDetail.getUsername())){
            // 如果username为空，则查询所有在线用户
            pageDataVo.setTotal((long) listAll.size());
            // 开始位置 = (当前页码-1)*每页条数
            // 取到当前要显示的Token去查询信息
            List<String> tokenList = SaUtils.searchTokenValue((currentPage - 1) * pageSize, pageSize, false);
            for (String token : tokenList) {
                UserLoginDetail userLoginDetail1 = (UserLoginDetail) SaUtils.getTokenSessionByToken(token).get(SaUtils.USER_LOGIN_DETAIL);
                userLoginDetailList.add(userLoginDetail1);
            }
        }else {
            // 搜索指定用户名的在线用户
            ArrayList<String> list = new ArrayList<>(); // 存储指定用户名的token
            listAll.forEach(token -> {
                if (SaUtils.getLoginUserNameByToken(token).equals(userLoginDetail.getUsername())) {
                    list.add(token);
                }
            });
            pageDataVo.setTotal((long) list.size());
            List<String> tokenList = ListUtils.pageList(list, currentPage, pageSize);
            for (String token : tokenList) {
                UserLoginDetail userLoginDetail1 = (UserLoginDetail) SaUtils.getTokenSessionByToken(token).get(SaUtils.USER_LOGIN_DETAIL);
                userLoginDetailList.add(userLoginDetail1);
            }
        }
        pageDataVo.setData(userLoginDetailList);
        return pageDataVo;
    }

    /**
     * 强制下线
     *
     * @param token
     */
    @Override
    public void kickOut(String token) {
        if (StringUtils.isBlank(token)){
            throw new CustomException("token不能为空");
        }
        SaUtils.logoutByTokenValue(token);
    }
}
