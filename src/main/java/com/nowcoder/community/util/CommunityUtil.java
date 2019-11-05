package com.nowcoder.community.util;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * @author xuming
 * @Date 2019/11/4 22:47
 */
public class CommunityUtil {
    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * md5加密
     * @param key 由用户输入的字符串 + salt拼接而成的新字符串
     * @return 经md5加密后得到的字符串
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
