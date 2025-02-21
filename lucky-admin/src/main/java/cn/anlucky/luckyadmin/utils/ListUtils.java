package cn.anlucky.luckyadmin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ArrayList 工具类
 */
public class ListUtils {

    /**
     * 分页查询方法，支持泛型类型
     *
     * @param <T>      泛型类型
     * @param list     要分页的列表
     * @param page     当前页码 (从1开始)
     * @param pageSize 每页显示的项目数量
     * @return 返回分页后的子列表
     */
    public static <T> List<T> pageList(List<T> list, int page, int pageSize) {
        if (list == null || page < 1 || pageSize < 1) {
            return new ArrayList<>();
        }
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, list.size());

        // 使用Stream API进行分页
        return IntStream.range(0, list.size())
                .filter(index -> index >= fromIndex && index < toIndex)
                .mapToObj(list::get)
                .collect(Collectors.toList());
    }

}
