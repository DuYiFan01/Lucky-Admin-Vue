package cn.anlucky.luckyadmin.system.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenPo {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 模块名
     */
    private String mouldName;
}
