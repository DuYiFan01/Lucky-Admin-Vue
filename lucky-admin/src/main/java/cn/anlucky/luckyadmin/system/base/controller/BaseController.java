package cn.anlucky.luckyadmin.system.base.controller;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.utils.page.PageDomain;
import cn.anlucky.luckyadmin.utils.page.TableSupport;
import cn.anlucky.luckyadmin.utils.page.vo.PageDataVo;
import cn.anlucky.luckyadmin.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class BaseController {
        /**
         * 将前台传递过来的日期格式的字符串，自动转化为Date类型
         */
        @InitBinder
        public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    setValue(DateUtils.parseDate(text));
                } catch (ParseException e) {
                    throw new CustomException(e.getMessage());
                }
            }
        });
    }

        /**
         * 设置请求分页数据
         */
        protected void startPage() {
        // 从ServeLet中拿分页参数，并封装到PageDomain对象中
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getCurrentPage();
        Integer pageSize = pageDomain.getPageSize();
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        pageDomain.setCurrentPage(pageNum);
        pageDomain.setPageSize(pageSize);
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

        /**
         * 获取当前第几页
         *
         * @return
         */
        protected Integer getCurrentPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        return pageDomain.getCurrentPage();
    }

        /**
         * 获取每页多少条数据
         *
         * @return
         */
        protected Integer getPageSize() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        return pageDomain.getPageSize();
    }

        /**
         * 获取分页数据
         * @return
         */
        protected PageDataVo getTableData(List<?> list) {
        PageDataVo pageDataVo = new PageDataVo();
        pageDataVo.setCurrentPage(getCurrentPage());
        pageDataVo.setPageSize(getPageSize());
        pageDataVo.setData(list);
        pageDataVo.setTotal(new PageInfo(list).getTotal());
        return pageDataVo;

    }


    }

