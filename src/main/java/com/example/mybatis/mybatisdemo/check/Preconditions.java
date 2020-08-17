package com.example.mybatis.mybatisdemo.check;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author wangrenzhe
 * @date 2020/8/6 17:14
 */
public class Preconditions {
    public static void checkArgument(boolean expression, String template, Object... args) {
        if (!expression) {
            throw new ParamException(String.format(template, args));
        }
    }

    public static void checkNotBlank(final CharSequence cs, String template, Object... args) {
        checkArgument(StringUtils.isNotBlank(cs), template, args);
    }

    public static void checkNotEmpty(Collection collection, String template, Object... args) {
        checkArgument(CollectionUtils.isNotEmpty(collection), template, args);
    }

    public static void checkNotEmpty(Map map, String template, Object... args) {
        checkArgument(MapUtils.isNotEmpty(map), template, args);
    }

}
