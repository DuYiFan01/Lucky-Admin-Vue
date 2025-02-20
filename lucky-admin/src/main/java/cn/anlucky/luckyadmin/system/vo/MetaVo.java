package cn.anlucky.luckyadmin.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaVo {
    private String title;
    private String icon;
    private boolean noCache;
    private String link;
}
