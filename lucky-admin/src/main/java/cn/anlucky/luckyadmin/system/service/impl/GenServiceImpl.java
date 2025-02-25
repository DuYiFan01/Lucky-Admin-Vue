package cn.anlucky.luckyadmin.system.service.impl;

import cn.anlucky.luckyadmin.exception.CustomException;
import cn.anlucky.luckyadmin.gen.config.GenerationPo;
import cn.anlucky.luckyadmin.gen.service.GenerationService;
import cn.anlucky.luckyadmin.system.mapper.TablePoMapper;
import cn.anlucky.luckyadmin.system.pojo.GenPo;
import cn.anlucky.luckyadmin.system.pojo.TablePo;
import cn.anlucky.luckyadmin.system.service.GenService;
import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.anlucky.luckyadmin.utils.satoken.SaTokenDaoUtils;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 代码生成Service实现类
 */
@Service
public class GenServiceImpl implements GenService {

    @Autowired
    private GenerationService generationService;
    @Autowired
    private GenerationPo generationPo;
    @Autowired
    private TablePoMapper tablePoMapper;


    /**
     * 获取所有表信息
     *
     * @param tablePo
     * @return
     */
    @Override
    public List<TablePo> showTableSatus(TablePo tablePo) {
        List<TablePo> tablePos = tablePoMapper.showTableSatus(tablePo);
        for (int i = 0; i < tablePos.size(); i++) {
            // 添加Entity字段，将数据库表名转换为实体名称
            tablePos.get(i).setEntity(StringUtils.convertToCamelCase(tablePos.get(i).getName()));
        }
        return tablePos;
    }

    /**
     * 预览代码
     *
     * @param genPo
     * @return
     */
    @Override
    public Map<String, String> previewCode(GenPo genPo) {
        if (genPo==null || StringUtils.isEmpty(genPo.getTableName())){
            throw new CustomException("参数或表名不能为空");
        }
        if (StringUtils.isEmpty(genPo.getPackageName())){
            genPo.setPackageName(generationPo.getPackageName());
        }
        if (StringUtils.isEmpty(genPo.getMouldName())){
            genPo.setMouldName(generationPo.getMouldName());
        }
        saveGenPo(genPo);
        Map<String, String> map = generationService.previewCode(genPo.getPackageName(), genPo.getMouldName(), genPo.getTableName());
        return map;
    }

    /**
     * 下载代码
     *
     * @param genPo
     * @return
     */
    @Override
    public byte[] downloadCode(GenPo genPo) {
        if (genPo==null || StringUtils.isEmpty(genPo.getTableName())){
            throw new CustomException("参数或表名不能为空");
        }
        if (StringUtils.isEmpty(genPo.getPackageName())){
            genPo.setPackageName(generationPo.getPackageName());
        }
        if (StringUtils.isEmpty(genPo.getMouldName())){
            genPo.setMouldName(generationPo.getMouldName());
        }
        saveGenPo(genPo);
        byte[] bytes = generationService.downloadCode(genPo.getPackageName(), genPo.getMouldName(), genPo.getTableName(), genPo.getTableName());
        return bytes;
    }

    /**
     * 保存代码生成包名，模块名信息到缓存中
     *
     * @param genPo
     * @return
     */
    @Override
    public void saveGenPo(GenPo genPo) {
        String loginId = SaUtils.getLoginId();
        SaTokenDaoUtils.setObjectKey(SaTokenDaoUtils.GENERATION_PACKAGE_CACHE+loginId,genPo);
    }

    /**
     * 从缓存中读取当前用户代码生成的包名和模块名，没有则使用默认配置
     *
     * @return
     */
    @Override
    public GenPo getGenPo() {
        String loginId = SaUtils.getLoginId();
        GenPo genPo = (GenPo) SaTokenDaoUtils.getObjectKey(SaTokenDaoUtils.GENERATION_PACKAGE_CACHE + loginId);
        if (genPo == null){
            // 如果缓存中没有，则取默认配置
            genPo = new GenPo();
            genPo.setPackageName(generationPo.getPackageName());
            genPo.setMouldName(generationPo.getMouldName());
        }
        return genPo;
    }
}
