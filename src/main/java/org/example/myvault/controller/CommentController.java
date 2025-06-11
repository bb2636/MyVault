package org.example.myvault.controller;

import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.example.myvault.domain.User;
import org.example.myvault.service.CollectionItemService;
import org.example.myvault.service.CommentService;
import org.example.myvault.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CollectionItemService collectionItemService;
    private final UserService userService;

    //한 글에 대한 댓글 등록
    @PostMapping("/add")
    public String addComment(@RequestParam Long itemId, @RequestParam String content) {
        User user = userService.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));
        CollectionItem item = collectionItemService.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));
        Comment comment = Comment.builder()
                .user(user)
                .collectionItem(item)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        commentService.save(comment);
        return "redirect:/items/" + itemId;
    }
}
