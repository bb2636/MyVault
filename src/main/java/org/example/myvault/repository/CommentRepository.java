package org.example.myvault.repository;

import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 CollectionItem에 달린 댓글 조회
    List<Comment> findByCollectionItem(@Param("item") CollectionItem item);
}
