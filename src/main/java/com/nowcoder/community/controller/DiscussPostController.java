package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xuming
 * @Date 2019/11/6 16:23
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发布帖子
     * @param title 标题
     * @param content 内容
     * @return
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            // 403表示没有权限
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 触发发帖事件 (涉及到Elasticsearch的内容)
        Event event = new Event()
            .setTopic(TOPIC_PUBLISH)
            .setUserId(user.getId())
            .setEntityType(ENTITY_TYPE_POST)
            .setEntityId(post.getId());
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, post.getId());

        // 暂不考虑报错的情况
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);

        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 帖子点赞数
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
        model.addAttribute("likeCount", likeCount);
        // 帖子点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 : likeService
            .findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, post.getId());
        model.addAttribute("likeStatus", likeStatus);

        // 评论分页信息
        // 每页至多显示5条评论
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        // 数据总条数
        page.setRows(post.getCommentCount());

        // 术语定义
        // 评论: 给帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentList = commentService
            .findCommentsByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论VO列表 (评论VO = 评论 + 发表评论的用户 + 回复[列表])
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 评论VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 发表评论的用户
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                // 评论点赞数
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                // 评论点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 : likeService
                    .findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus", likeStatus);

                // 回复列表
                List<Comment> replyList = commentService
                    .findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);// 回复不进行分页
                // 回复VO列表 (回复VO = 回复 + 发表回复的用户 [+ 被回复的用户])
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 发表回复的用户
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 被回复的用户
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        // 回复点赞数
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        // 回复点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService
                            .findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }

    // 设置帖子置顶
    @RequestMapping(path = "/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTop(int id) {
        discussPostService.updateType(id, 1);

        // (版主)触发发帖事件以更新es
        Event event = new Event()
            .setTopic(TOPIC_PUBLISH)
            .setUserId(hostHolder.getUser().getId())
            .setEntityType(ENTITY_TYPE_POST)
            .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }

    // (版主)给帖子加精(华)
    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(int id) {
        discussPostService.updateStatus(id, 1);

        // 触发发帖事件以更新es
        Event event = new Event()
            .setTopic(TOPIC_PUBLISH)
            .setUserId(hostHolder.getUser().getId())
            .setEntityType(ENTITY_TYPE_POST)
            .setEntityId(id);
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return CommunityUtil.getJSONString(0);
    }

    // 管理员删帖
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);

        // 触发删帖事件以将相应内容从es中删除
        Event event = new Event()
            .setTopic(TOPIC_DELETE)
            .setUserId(hostHolder.getUser().getId())
            .setEntityType(ENTITY_TYPE_POST)
            .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);

    }

}
