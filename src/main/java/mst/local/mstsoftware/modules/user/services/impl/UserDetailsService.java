package mst.local.mstsoftware.modules.user.services.impl;

import lombok.RequiredArgsConstructor;
import mst.local.mstsoftware.modules.user.entities.User;
import mst.local.mstsoftware.modules.user.repositories.UserRepository;

import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        var authorities = java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .authorities(authorities)
                .build();

    }

}
