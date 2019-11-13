package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xuming
 * @Date 2019/11/4 9:11
 */
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    /**
     * 统计行数 (分页逻辑需要)
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}
