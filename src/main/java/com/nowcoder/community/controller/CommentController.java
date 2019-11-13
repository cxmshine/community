package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author xuming
 * @Date 2019/11/7 10:19
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Model model, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0); // 0表示有效
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件,发送站内通知
        Event event = new Event()
            .setTopic(TOPIC_COMMENT)
            .setUserId(hostHolder.getUser().getId())
            .setEntityType(comment.getEntityType())
            .setEntityId(comment.getEntityId())
            .setData("postId", discussPostId);
        // 究竟是对帖子进行评论还是对回复进行评论
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT){
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        // 如果是对帖子发布评论(而不是对回复发布评论)
        // 涉及到Elasticsearch的内容
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // 触发发帖事件 (实际上是更新)
            event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(comment.getUserId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
            // 计算帖子分数 (若是对回复发布评论,不涉及到分数的重新计算)
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, discussPostId);
        }

        // 评论发布完成后,重定向到帖子详情页
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
