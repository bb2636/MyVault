package org.example.myvault.controller;

import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.service.CollectionItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final CollectionItemService collectionItemService;

    @GetMapping("/items")
    public String listItems(Model model) {
        List<CollectionItem> items = collectionItemService.findAll();
        model.addAttribute("items", items);
        return "items/list";
    }

//    @PostMapping("/items/add")
//    public String addItem(CollectionItem item) {
//        collectionItemService.save(item);
//        return "redirect:/items";
//    }

}
