package com.nowcoder.community.util;

/**
 * @author xuming
 * @Date 2019/11/8 9:20
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    /** 对帖子/评论进行点赞 */
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_ENTITY_USER = "like:user";

    /** 某个实体的赞 */
    /** like:entity:entityType:entityId -> set(存放userId)*/
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /** 某个用户收到的赞 */
    /** like:user:userId -> int */
    public static String getUserLikeKey(int userId) {
        return PREFIX_ENTITY_USER + SPLIT + userId;
    }
}
