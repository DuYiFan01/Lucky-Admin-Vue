package cn.anlucky.luckyadmin.system.mapper;

import cn.anlucky.luckyadmin.system.pojo.SysFiles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yifan.du
 * @since 2025-02-25 16:19:40
 */
public interface SysFilesMapper extends BaseMapper<SysFiles> {


    public List<String> getFileAbsPath(Long businessId, String fileBusinessType);
    public String getFileAbsPathById(Long fileBusinessId);

}
