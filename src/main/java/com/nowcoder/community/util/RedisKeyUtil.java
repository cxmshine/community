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
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    /** UV (Unique Vistor) */
    private static final String PREFIX_UV = "uv";
    /** DAU (Daily Active User) */
    private static final String PREFIX_DAU = "dau";

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

    /** 某个用户的关注 */
    /** followee:userId:entityType -> zset(entityId, now) */
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /** 某个实体的拥有的粉丝 */
    /** follower:entityType:entityId -> zset(userId, now) */
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /** 登录验证码 */
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /** 登录凭证 */
    public static String geTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    /** 用户 */
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日UV
    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    // 区间UV
    public static String getUVKey(String startDate, String endDate) {
        return PREFIX_UV + SPLIT + startDate + SPLIT + endDate;
    }

    // 单日活跃用户
    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    // 区间活跃用户
    public static String getDAUKey(String startDate, String endDate) {
        return PREFIX_DAU + SPLIT + startDate + SPLIT + endDate;
    }
}
