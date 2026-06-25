package mst.local.mstsoftware.helpers;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mst.local.mstsoftware.services.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       

        // 1. Lấy token từ header
        String authHeader = request.getHeader("Authorization");

        // 2. Không có token hoặc không đúng format -> bỏ qua, đi tiếp
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // cho qua, không làm gì cả
            return;
        }

        // 3. Extract Token
        String token = authHeader.substring(7);
        // System.out.println("Token: [" + token + "]");
        String email = jwtService.extractEmail(token);
        // System.out.println("Email: [" + email + "]");
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // truy xuất metadata
                                                                                                  // request
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());

            }
        }

        filterChain.doFilter(request, response);
    }
}
