package mst.local.mstsoftware.modules.user.services.impl;

import lombok.RequiredArgsConstructor;
import mst.local.mstsoftware.modules.user.entities.User;
import mst.local.mstsoftware.modules.user.entities.UserRole;
import mst.local.mstsoftware.modules.user.repositories.UserRepository;
import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
//        var authorities = java.util.List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toString()));
        List<GrantedAuthority> authorities = user.getUserRoles().stream()
                .filter(UserRole::isActive)
                .map(ur -> (GrantedAuthority)
                        new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName())
                )
                .collect(Collectors.toList());
        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .authorities(authorities)
                .build();

    }

}
