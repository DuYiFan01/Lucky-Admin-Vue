package cn.anlucky.luckyadmin.system.enums;


import lombok.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件上传业务类型
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum FileBusinessType {
    USER_AVATAR("USER_AVATAR", "用户头像");

    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 获取所有文件业务类型
     */
    public static Map<String,String> getAllFileBusinessType() {
        FileBusinessType[] values = FileBusinessType.values();
        return Arrays.stream(values).collect(Collectors.toMap(FileBusinessType::getBusinessType, FileBusinessType::getBusinessName));
    }

}
