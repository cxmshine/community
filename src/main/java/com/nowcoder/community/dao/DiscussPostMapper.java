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
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 统计行数 (分页逻辑需要)
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);
}
