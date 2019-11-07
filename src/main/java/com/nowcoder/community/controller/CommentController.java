package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.util.HostHolder;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Model model, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0); // 0表示有效
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        // 评论发布完成后,重定向到帖子详情页
        return "redirect:/discuss/detail/" + discussPostId;
    }
}
