package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xuming
 * @Date 2019/11/6 22:31
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);
}
