package cn.anlucky.luckyadmin.system.config;


import cn.anlucky.luckyadmin.gen.config.GenerationPo;
import cn.anlucky.luckyadmin.utils.StringUtils;
import cn.anlucky.luckyadmin.utils.satoken.SaUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * mybatis-plus配置
 */
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {

    @Autowired
    private GenerationPo generationPo;

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        return interceptor;
    }

    /**
     * @param metaObject
     * 插入数据自动为加了 @TableField(fill = FieldFill.INSERT) 的字段赋值
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 取得填充字段
        String createByField = StringUtils.toCamelCase(generationPo.getCreateByField());
        String createTimeField = StringUtils.toCamelCase(generationPo.getCreateTimeField());
        String loginUserName = getLoginUserName();

        // 如果有某些字段则进行填充，没有则不进行填充
        if (metaObject.hasGetter(createByField)){
            this.setFieldValByName(createByField, loginUserName, metaObject);
        }

        if (metaObject.hasGetter(createTimeField)){
            this.setFieldValByName(createTimeField,LocalDateTime.now(), metaObject);
        }
        // 同步添加更新的内容
        this.updateFill(metaObject);
    }

    /**
     * @param metaObject
     * 更新世情自动为加了 @TableField(fill = FieldFill.UPDATE) 的字段赋值
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String updateByField = StringUtils.toCamelCase(generationPo.getUpdateByField());
        String updateTimeField = StringUtils.toCamelCase(generationPo.getUpdateTimeField());
        // 登录的情况取登录用户名，未登录的情况取system
        String loginUserName = getLoginUserName();
        if (metaObject.hasGetter(updateTimeField)){
            this.setFieldValByName( updateTimeField, LocalDateTime.now(),metaObject );
        }
        if (metaObject.hasGetter(updateByField)){
            this.setFieldValByName(updateByField,loginUserName , metaObject);
        }
    }

    /**
     * 获取登录用户名
     * @return
     */
    private String getLoginUserName() {
        String loginUserName = null;
        try {
            loginUserName = SaUtils.isLogin()? SaUtils.getLoginUserName() : "system";
        }catch (Exception e){
            loginUserName = "system";
        }
        return loginUserName;
    }


}
