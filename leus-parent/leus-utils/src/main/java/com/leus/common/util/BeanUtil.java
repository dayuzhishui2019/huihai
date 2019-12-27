package com.leus.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据bean工具类
 *
 * @author duanyihui
 */
public class BeanUtil {

    private static final String DEFAULT_PROPERTY = "class";

    private static final int HAS_NULL = 1;

    private static final int HAS_VALUE = 2;

    /**
     * 私有构造器
     */
    private BeanUtil() {
    }

    /**
     * 判断bean的属性是否有null值
     *
     * @param o            对象
     * @param exceptFields 不校验的属性名称
     * @return
     */
    public static boolean isPropertiesHasNull(Object o, String... exceptFields) {
        return checkPropertiesHasNull(HAS_NULL, o, exceptFields);
    }

    /**
     * 判断bean中必须的属性是否有值
     *
     * @param o     对象
     * @param field 检查的属性
     * @return
     */
    public static boolean isNecessaryPropertiesHasValue(Object o, String... field) {
        return checkPropertiesHasNull(HAS_VALUE, o, field);
    }

    private static boolean checkPropertiesHasNull(int type, Object o, String... f) {
        boolean result = true;
        int i = 0;
        try {
            List<String> efs = Lists.newArrayList(f);
            efs.add(DEFAULT_PROPERTY);
            BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : pds) {
                String name = propertyDescriptor.getName();
                if (type == 1 && efs.contains(name)) {
                    continue;
                } else if (!(type == 2 && efs.contains(name))) {
                    continue;
                }
                Object value = propertyDescriptor.getReadMethod().invoke(o);
                result = result && type == 1 ? value == null : value != null;
                i++;
                if (!result) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i == 0 ? false : result;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> c, Class<T> a) {
        Preconditions.checkNotNull(c);
        Preconditions.checkNotNull(a);
        return c.getAnnotation(a);
    }

    public static Map<String, Object> toMap(Object bean) {
        String json = JSON.toJSONString(bean);
        return JSON.parseObject(json, new TypeReference<HashMap<String, Object>>() {
        });

    }

}