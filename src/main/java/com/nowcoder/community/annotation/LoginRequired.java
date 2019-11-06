package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xuming
 * @Date 2019/11/6 13:14
 * 检查登录状态
 */
@Target(ElementType.METHOD) // 作用于方法之上
@Retention(RetentionPolicy.RUNTIME) // 运行时起作用即可
public @interface LoginRequired {
}
