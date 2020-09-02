package com.yjy.superbridge.internal.convert;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/09/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface ChildConvertFactory<T,F> {
    void walk(String key,Object value);


}
