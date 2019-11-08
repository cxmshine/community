package com.nowcoder.community.util;

/**
 * @author xuming
 * @Date 2019/11/8 9:20
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    /** 对帖子/评论进行点赞 */
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    /** 某个实体的赞 */
    /** like:entity:entityType:entityId -> set(存放userId)*/
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
