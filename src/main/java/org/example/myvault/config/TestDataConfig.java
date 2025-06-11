package org.example.myvault.config;

import org.example.myvault.domain.CollectionItem;
import org.example.myvault.domain.Comment;
import org.example.myvault.domain.User;
import org.example.myvault.repository.CollectionItemRepository;
import org.example.myvault.repository.CommentRepository;
import org.example.myvault.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataConfig {
//    @Bean
    public CommandLineRunner testData(
            UserRepository userRepository,
            CollectionItemRepository collectionItemRepository,
            CommentRepository commentRepository) {
        return args -> {
            User user = User.builder()
                    .nickname("testUser")
                    .password("1234")
                    .build();
            userRepository.save(user);

            CollectionItem item = CollectionItem.builder()
                    .user(user)
                    .title("My Favorite Figure")
                    .description("This is my favorite figure!")
                    .image("figure.jpg")
                    .isPrivate(false)
                    .build();
            collectionItemRepository.save(item);

            Comment comment = Comment.builder()
                    .user(user)
                    .collectionItem(item)
                    .content("Nice collection!")
                    .build();
            commentRepository.save(comment);
        };
    }
}