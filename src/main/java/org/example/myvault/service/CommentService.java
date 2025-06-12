package org.example.myvault.service;

import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.example.myvault.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findByCollectionItem(CollectionItem item) {
        return commentRepository.findByCollectionItem(item);
    }
    public Optional<Comment> findById(Long id) {return commentRepository.findById(id);}
    public void delete(Comment comment) {commentRepository.delete(comment);}
}
