package cn.anlucky.luckyadmin.system.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysCache {

    /**
     * 缓存名称
     */
    private String cacheName = "";
    /**
     * 缓存键名
     */
    private String cacheKey = "";
    /** 缓存内容 */
    private String cacheValue = "";

    /** 备注 */
    private String remark = "";

    public SysCache(String cacheName,String remark){
        this.cacheName = cacheName;
        this.remark = remark;
    }
}
