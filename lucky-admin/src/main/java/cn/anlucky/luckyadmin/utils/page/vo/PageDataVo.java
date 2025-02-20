package cn.anlucky.luckyadmin.utils.page.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页数据实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDataVo {
    private Integer currentPage;
    private Integer pageSize;
    private Long total;
    private Object data;
}
