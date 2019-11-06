package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
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

    /**
     * JSONObject转为JSONString
     * @param code 状态码
     * @param msg 提示信息
     * @param map 附加信息(业务数据)
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        }
        return jsonObject.toJSONString();
    }
    /** 方法重载 */
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }
    /** 方法重载 */
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }



}
