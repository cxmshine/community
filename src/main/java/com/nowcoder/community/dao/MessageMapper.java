package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xuming
 * @Date 2019/11/7 11:19
 */
@Mapper
public interface MessageMapper {

    // 查询当前用户的会话列表,页面上针对每个会话只显示最新的一条私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表 (私信详情页需要展示)
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增消息
    int insertMessage(Message message);

    /**
     * 修改消息的状态
     * @param ids 若有多条未读私信,点开私信详情页后,需要将这些私信的状态都改为已读
     * @param status
     * @return
     */
    int updateStatus(List<Integer> ids, int status);

    // 查询某个主题(点赞/评论/关注)下最新的通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某个主题包含的通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读的通知数量
    int selectNoticeUnreadCount(int userId, String topic);

    // 查询某个主题(点赞/评论/关注)所包含的通知列表
    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
