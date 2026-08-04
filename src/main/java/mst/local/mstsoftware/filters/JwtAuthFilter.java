package mst.local.mstsoftware.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import mst.local.mstsoftware.modules.user.resources.CustomUserDetails;
import mst.local.mstsoftware.modules.user.services.impl.PermissionCacheService;
import mst.local.mstsoftware.modules.user.services.impl.UserSessionCache;
import mst.local.mstsoftware.resources.ApiResource;
import mst.local.mstsoftware.resources.ErrorResource;
import mst.local.mstsoftware.services.interfaces.BlacklistServiceInterface;
import mst.local.mstsoftware.services.interfaces.JwtServiceInterface;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j // cài sẵn logger
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtServiceInterface jwtService;
    private final BlacklistServiceInterface blacklistService;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final PermissionCacheService permissionCacheService;
    private final UserSessionCache userSessionCache;

    public static final String TOKEN_ATTRIBUTE = "jwt_token";

    private static final Map<Class<? extends JwtException>, String> JWT_ERRORS_MESSAGES = Map.of(
            MalformedJwtException.class, "Định dạng token không hợp lệ",
            ExpiredJwtException.class, "Token đã hết hạn",
            SignatureException.class, "Token không được tạo bởi hệ thống này",
            UnsupportedJwtException.class, "Loại token không được hỗ trợ");

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
        try {
            String jti = jwtService.extractJti(token);
            if (Boolean.TRUE.equals(blacklistService.isRevoked(jti))) {
                writeErrorResponse(response, "Token của bạn không hợp lệ.");
                return;
            }
            request.setAttribute(TOKEN_ATTRIBUTE, token);
            Long userId = jwtService.extractSubject(token);
//            List<RoleType> roles = jwtService.extractRoles(token);


            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Map<String, Object> session = userSessionCache.get(userId);

                if (session == null) {
                    // Session hết hạn hoặc bị evict → force logout
                    writeErrorResponse(response, "Phiên đăng nhập đã hết hạn.");
                    return;
                }
                String email = (String) session.get("email");
                List<String> roles = (List<String>) session.get("roles");

                // Load permissions từ Redis cache theo roles
                Set<String> permissions = permissionCacheService.getPermissionsByRoles(
                        roles.stream().map(RoleType::valueOf).collect(Collectors.toList())
                );

                // Build authorities
                Set<GrantedAuthority> authorities = new HashSet<>();
                roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
                permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));

                // Build principal
                CustomUserDetails userDetails = CustomUserDetails.builder()
                        .id(userId)
                        .email(email)
                        .authorities(authorities)
                        .build();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

//                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//                if (jwtService.isTokenValid(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // truy xuất
//                    // metadata
//                    // request
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                }
            }
        } catch (JwtException e) {
            String message = JWT_ERRORS_MESSAGES.getOrDefault(e.getClass(), "Lỗi xác thực token 11!");
            writeErrorResponse(response, message);
            return;

        } catch (Exception e) {
            writeErrorResponse(response, "Lỗi xác thực token 12!");
            return;
        }
        filterChain.doFilter(request, response);

    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorResource error = ErrorResource.builder()
                .code("UNAUTHORIZED")
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResource.error(error, message)));
    }
}
