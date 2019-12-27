package com.leus.common.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author sue  2017/8/14
 */
public class TypeUtil
{
    private TypeUtil()
    {
    }

    private static final List<Class<?>> basicTypes;

    static
    {
        List<Class<?>> classes = Lists.newArrayList();
        classes.add(String.class);
        classes.add(Character.class);
        classes.add(Byte.class);
        classes.add(Short.class);
        classes.add(Integer.class);
        classes.add(Long.class);
        classes.add(Float.class);
        classes.add(Double.class);
        basicTypes = ImmutableList.copyOf(classes);
    }

    public static boolean isBasicType(Class<?> clz)
    {
        return basicTypes.contains(clz) || clz.isPrimitive();
    }

    public static boolean isArray(Class<?> clz)
    {
        return clz.isArray();
    }

    public static boolean isInterface(Class<?> clz)
    {
        return clz.isInterface();
    }

    public static boolean instanceOf(Object target, Class<?> clz)
    {
        return clz.isInstance(target);
    }

    public static boolean isSynthetic(Class<?> clz)
    {
        return clz.isSynthetic();
    }

    public static boolean isEnum(Class<?> clz)
    {
        return clz.isEnum();
    }

}
