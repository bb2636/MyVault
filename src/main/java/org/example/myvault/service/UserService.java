package org.example.myvault.service;

import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.User;
import org.example.myvault.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User save(User user) {
        return userRepository.save(user);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }
}
