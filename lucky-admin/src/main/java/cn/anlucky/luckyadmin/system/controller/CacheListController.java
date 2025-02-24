package cn.anlucky.luckyadmin.system.controller;

import cn.anlucky.luckyadmin.system.pojo.SysCache;
import cn.anlucky.luckyadmin.utils.satoken.SaTokenDaoUtils;
import cn.anlucky.luckyadmin.vo.R;
import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "缓存列表Controller")
@RequiredArgsConstructor
@RequestMapping("/monitor/cacheList")
public class CacheListController {


    private final static List<SysCache> caches = new ArrayList<SysCache>();
    {
        caches.add(new SysCache(SaTokenDaoUtils.PREFIX + ":" + SaTokenDaoUtils.ROLES_CACHE, "用户角色"));
        caches.add(new SysCache(SaTokenDaoUtils.PREFIX + ":" + SaTokenDaoUtils.PERMISSIONS_CACHE, "角色权限"));
        caches.add(new SysCache(SaTokenDaoUtils.PREFIX + ":" + SaTokenDaoUtils.ROUTER_CACHE, "角色路由"));
        caches.add(new SysCache(SaTokenDaoUtils.PREFIX + ":" + SaTokenDaoUtils.CAPTCHA_CACHE, "验证码"));
        caches.add(new SysCache(SaTokenDaoUtils.PREFIX + ":" + SaTokenDaoUtils.GENERATION_PACKAGE_CACHE, "代码生成"));
    }

    /**
     * 查询缓存名字列表
     * @return List<SysCache>
     */
    @Operation(summary = "查询可显示缓存列表")
    // @SaCheckPermission("monitor::cacheList::query")
    @GetMapping("/nameList")
    public R nameList() {
        return R.ok(caches);
    }

    /**
     * 获取缓存Key列表
     * @param cacheName 缓存名称
     * @return SysLoginLog
     */
    @Operation(summary = "获取缓存Key列表")
    // @SaCheckPermission("monitor::cacheList::query")
    @GetMapping("/keyList/{cacheName}")
    public R keyListByCacheName(@PathVariable(name = "cacheName") String cacheName) {
        List<String> keys = SaTokenDaoUtils.getSaTokenDao().searchData(cacheName, "",0, -1,false);
        return R.ok(keys);
    }

    /**
     * 获取缓存值
     * @param cacheName 缓存名称
     * @param cacheKey 缓存key
     * @return SysCache 缓存值
     */
    @Operation(summary = "获取缓存值")
    // @SaCheckPermission("monitor::cacheList::query")
    @GetMapping("/cacheValue/{cacheName}/{cacheKey}")
    public R cacheValueByKey(@PathVariable(name = "cacheName") String cacheName, @PathVariable(name = "cacheKey") String cacheKey) {
        Object objectKey = SaTokenDaoUtils.getSaTokenDao().getObject(cacheKey);
        SysCache sysCache = new SysCache();
        sysCache.setCacheName(cacheName);
        sysCache.setCacheKey(cacheKey);
        if (objectKey != null) {
            sysCache.setCacheValue(JSON.toJSONString(objectKey));
        } else {
            sysCache.setCacheValue("");
        }
        return R.ok(sysCache);
    }

    /**
     * 删除指定名字的缓存
     * @param cacheName 缓存名称
     * @return 删除缓存成功
     */
    @Operation(summary = "删除指定名字的缓存")
    // @SaCheckPermission("monitor::cacheList::delete")
    @GetMapping("/deleteCacheName/{cacheName}")
    public R deleteCacheNameByCacheName(@PathVariable(name = "cacheName") String cacheName) {
        List<String> list = SaTokenDaoUtils.getSaTokenDao().searchData(cacheName, "", 0, -1, false);
        list.forEach(key -> {
            SaTokenDaoUtils.getSaTokenDao().deleteObject(key);
        });
        return R.ok("删除缓存成功");
    }

    /**
     * 删除指定Key缓存
     * @param cacheKey
     * @return
     */
    @Operation(summary = "删除指定Key缓存")
    // @SaCheckPermission("monitor::cacheList::delete")
    @GetMapping("/deleteCacheKey/{cacheKey}")
    public R deleteCacheKeyByCacheKey(@PathVariable(name = "cacheKey") String cacheKey) {
        SaTokenDaoUtils.getSaTokenDao().deleteObject(cacheKey);
        return R.ok("删除缓存成功");
    }

    /**
     * 清空所有缓存
     * @return
     */
    @Operation(summary = "清空缓存")
    // @SaCheckPermission("monitor::cacheList::delete")
    @GetMapping("/deleteCacheAll")
    public R deleteCacheAll() {
        caches.forEach(sysCache -> {
            List<String> list = SaTokenDaoUtils.getSaTokenDao().searchData(sysCache.getCacheName(), "", 0, -1, false);
            list.forEach(key -> {
                SaTokenDaoUtils.getSaTokenDao().deleteObject(key);
            });
        });
        return R.ok("删除缓存成功");
    }


}
