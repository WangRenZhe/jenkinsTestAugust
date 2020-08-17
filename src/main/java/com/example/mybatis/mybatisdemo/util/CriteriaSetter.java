package com.example.mybatis.mybatisdemo.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author wangrenzhe
 * @date 2020/8/17 17:27
 */
public class CriteriaSetter {

    public static <T> void setEqual(T value, Consumer<T> criteriaSetter) {
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            if (StringUtils.isNoneBlank((String)value)) {
                criteriaSetter.accept(value);
            }
        } else {
            criteriaSetter.accept(value);
        }
    }

    public static <T> void setIn(List<T> list, Consumer<List<T>> criteriaSetter) {
        if (CollectionUtils.isNotEmpty(list)) {
            criteriaSetter.accept(list);
        }
    }

    public static void setLike(String value, Consumer<String> criteriaSetter) {
        if (StringUtils.isNotBlank(value)) {
            criteriaSetter.accept("%" + value + "%");
        }
    }

}
