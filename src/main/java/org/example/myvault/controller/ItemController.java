package org.example.myvault.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.example.myvault.domain.User;
import org.example.myvault.service.CollectionItemService;
import org.example.myvault.service.CommentService;
import org.example.myvault.service.UserService;
import org.springframework.beans.factory.annotation.Value;
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

    //이미지 파일 저장 경로
    @Value("${myvault.upload.path}")
    private String uploadPath;
    //등록
    @PostMapping("/add")
    public String addItem(@ModelAttribute CollectionItem item,
                          @RequestParam("imageFile")MultipartFile imageFile) throws IOException {
        //임시 사용자 id = 1
        User user = userService.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));
        item.setUser(user);
        item.setCreatedAt(LocalDateTime.now());

        //이미지 저장
        if(!imageFile.isEmpty()) {
            //저장경로
            File dir = new File(uploadPath);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            //파일명
            String fileName = imageFile.getOriginalFilename();
            String newFileName = UUID.randomUUID() + "_" + fileName;
            //저장
            imageFile.transferTo(new File(uploadPath + newFileName));
            item.setImage(newFileName); //db에는 파일명만 저장
        }
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

    //삭제
    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        CollectionItem item = collectionItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id"));
        //이미지 삭제
        if(item.getImage() != null) {
            //저장된 이미지 불러옴
            File imageFile = new File(uploadPath + item.getImage());
            if(imageFile.exists()) {
                imageFile.delete();
            }
        }
        //db 삭제
        collectionItemService.delete(item);
        return "redirect:/items";
    }
}
