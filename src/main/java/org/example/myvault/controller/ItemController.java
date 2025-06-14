package org.example.myvault.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.myvault.config.CustomUserDetails;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.example.myvault.domain.User;
import org.example.myvault.service.CollectionItemService;
import org.example.myvault.service.CommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final CollectionItemService collectionItemService;
    private final CommentService commentService;

    @Value("${myvault.upload.path}")
    private String uploadPath;

    // 전체 조회
    @GetMapping
    public String listItems(Model model) {
        List<CollectionItem> items = collectionItemService.findAll();
        List<CollectionItem> recentItems = collectionItemService.findTop3ByOrderByCreatedAtDesc();

        model.addAttribute("items", items);
        model.addAttribute("recentItems", recentItems);

        return "items/list";
    }

    // 상세 조회
    @GetMapping("/{id}")
    public String getItem(@PathVariable Long id,
                          @RequestParam(value = "editCommentId", required = false) Long editCommentId,
                          Model model,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));
        List<Comment> comments = commentService.findByCollectionItem(item);
        Long currentUserId = (userDetails != null) ? userDetails.getUser().getId() : null;

        model.addAttribute("item", item);
        model.addAttribute("comments", comments);
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("editCommentId", editCommentId);

        return "items/detail";
    }

    // 등록 폼
    @GetMapping("/add")
    public String showAddForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/users/login";
        }
        model.addAttribute("item", new CollectionItem());
        return "items/form";
    }

    // 등록 처리
    @PostMapping("/add")
    public String addItem(@ModelAttribute CollectionItem item,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        if (userDetails == null) {
            throw new IllegalStateException("로그인한 사용자만 등록 가능합니다");
        }
        User user = userDetails.getUser();
        item.setUser(user);
        item.setCreatedAt(LocalDateTime.now());

        if (!imageFile.isEmpty()) {
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            String fileName = imageFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + fileName;
            imageFile.transferTo(new File(uploadPath + newFileName));
            item.setImage(newFileName);
        }
        collectionItemService.save(item);
        return "redirect:/items";
    }

    // 마이 아이템
    @GetMapping("/my-items")
    public String myItems(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/users/login";
        }
        User user = userDetails.getUser();
        List<CollectionItem> myItems = collectionItemService.findByUser(user);
        model.addAttribute("items", myItems);
        return "items/my-item";
    }

    // 삭제
    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));

        if (item.getImage() != null) {
            File imageFile = new File(uploadPath + item.getImage());
            if (imageFile.exists()) imageFile.delete();
        }
        collectionItemService.delete(item);
        return "redirect:/items";
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));
        model.addAttribute("item", item);
        return "items/edit-form";
    }

    // 수정 처리
    @PostMapping("/{id}/edit")
    public String editItem(@PathVariable Long id,
                           @ModelAttribute CollectionItem itemForm,
                           @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item id"));

        item.setTitle(itemForm.getTitle());
        item.setDescription(itemForm.getDescription());
        item.setPrivate(itemForm.isPrivate());

        if (imageFile != null && !imageFile.isEmpty()) {
            if (item.getImage() != null) {
                File oldImage = new File(uploadPath + item.getImage());
                if (oldImage.exists()) oldImage.delete();
            }
            String fileName = imageFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + fileName;
            imageFile.transferTo(new File(uploadPath + newFileName));
            item.setImage(newFileName);
        }
        collectionItemService.save(item);
        return "redirect:/items/" + id;
    }

    // 이미지 삭제
    @GetMapping("/{id}/delete-image")
    public String deleteImage(@PathVariable Long id) {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));

        if (item.getImage() != null) {
            File oldImage = new File(uploadPath + item.getImage());
            if (oldImage.exists()) oldImage.delete();
            item.setImage(null);
        }
        collectionItemService.save(item);
        return "redirect:/items/" + id + "/edit";
    }
}
