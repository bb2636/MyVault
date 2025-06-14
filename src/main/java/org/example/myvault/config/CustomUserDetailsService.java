package org.example.myvault.config;

import lombok.RequiredArgsConstructor;
import org.example.myvault.domain.User;
import org.example.myvault.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        System.out.println("🔐 로그인 시도 nickname: " + nickname);
        return userRepository.findByNickname(nickname)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다: " + nickname));
    }
}