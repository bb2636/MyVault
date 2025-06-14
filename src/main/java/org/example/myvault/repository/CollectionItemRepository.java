package org.example.myvault.repository;

import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long> {
    // 마이페이지 용: 해당 유저의 소장품 전체 조회
    List<CollectionItem> findByUser(User user);
    //최근 등록된 3개 보여주기
    List<CollectionItem> findTop3ByOrderByCreatedAtDesc();
    // 공개 게시판 용: 공개된 소장품 전체 조회
    List<CollectionItem> findByIsPrivateFalse();
}
