package com.yjy.superbridge.internal;

/**
 * <pre>
 *     author : yjy
 *     e-mail : yujunyu12@gmail.com
 *     time   : 2020/08/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface IBridgeFactory<T extends ReceiveFromPlatformCallback> {
    IBridgeCore getBridgeCore();
    IBridgeClient getBridgeClient();

    IBridgeFactory setReceiveFromPlatformCallback(T callback);

    T getReceiveFromPlatformCallback();

    void install();
}
