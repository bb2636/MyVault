package org.example.myvault.controller;

import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.example.myvault.service.CollectionItemService;
import org.example.myvault.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final CollectionItemService collectionItemService;
    private final CommentService commentService;

    //전체조회
    @GetMapping("/items")
    public String listItems(Model model) {
        List<CollectionItem> items = collectionItemService.findAll();
        model.addAttribute("items", items);
        return "items/list";
    }

    //상세조회
    @GetMapping("/items/{id}")
    public String getItem(@PathVariable Long id, Model model) {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));
        List<Comment> comments = commentService.findByCollectionItem(item);
        model.addAttribute("item", item);
        model.addAttribute("comments", comments);
        return "items/detail";
    }

//    @PostMapping("/items/add")
//    public String addItem(CollectionItem item) {
//        collectionItemService.save(item);
//        return "redirect:/items";
//    }

}
