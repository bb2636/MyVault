package org.example.myvault.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.example.myvault.domain.User;
import org.example.myvault.service.CollectionItemService;
import org.example.myvault.service.CommentService;
import org.example.myvault.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final CollectionItemService collectionItemService;
    private final CommentService commentService;
    private final UserService userService;

    //전체조회
    @GetMapping
    public String listItems(Model model) {
        List<CollectionItem> items = collectionItemService.findAll();
        model.addAttribute("items", items);
        return "items/list";
    }

    //상세조회
    @GetMapping("/{id}")
    public String getItem(@PathVariable Long id, Model model) {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));
        List<Comment> comments = commentService.findByCollectionItem(item);
        model.addAttribute("item", item);
        model.addAttribute("comments", comments);
        return "items/detail";
    }

    //등록 페이지 조회
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("item", new CollectionItem());
        return "items/form";
    }

    //등록
    @PostMapping("/add")
    public String addItem(@ModelAttribute CollectionItem item) {
        //임시 사용자 id = 1
        User user = userService.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));
        item.setUser(user);
        item.setCreatedAt(LocalDateTime.now());
        collectionItemService.save(item);
        return "redirect:/items";
    }

    //마이페이지용 조회
    @GetMapping("/my-items")
    public String myItems(Model model) {
        User user = userService.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));

        List<CollectionItem> myItem = collectionItemService.findByUser(user);
        log.info("조회된 아이템 수: {}", myItem.size());
        model.addAttribute("items", myItem);
        return "items/my-item";
    }
}
