package org.example.myvault.service;

import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.User;
import org.example.myvault.repository.CollectionItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectionItemService {
    private final CollectionItemRepository collectionItemRepository;

    public CollectionItem save(CollectionItem item) {
        return collectionItemRepository.save(item);
    }

    public List<CollectionItem> findAll() {
        return collectionItemRepository.findAll();
    }

    public Optional<CollectionItem> findById(Long id) {
        return collectionItemRepository.findById(id);
    }

    public List<CollectionItem> findByUser(User user) {
        return collectionItemRepository.findByUser(user);
    }
}
