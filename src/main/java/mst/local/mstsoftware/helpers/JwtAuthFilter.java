package mst.local.mstsoftware.helpers;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.users.services.impl.BlacklistService;
import mst.local.mstsoftware.resources.ErrorResource;
import mst.local.mstsoftware.services.JwtService;

@Component
@Slf4j // cài sẵn logger
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final BlacklistService blacklistService;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    public static final String TOKEN_ATTRIBUTE = "jwt_token";

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
        if (blacklistService.isTokenBlackList(token)) {
            writeErrorResponse(response, "Token của bạn đã bị thu hồi.");
            return;
        }
        request.setAttribute(TOKEN_ATTRIBUTE, token);
        try {
            String email = jwtService.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // truy xuất
                                                                                                      // metadata
                                                                                                      // request
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // System.out.println("Auth: " +
                    // SecurityContextHolder.getContext().getAuthentication());

                }
            }
        } catch (MalformedJwtException e) {
            writeErrorResponse(response, "Định dạng token không hợp lệ");
            return;
        } catch (ExpiredJwtException e) {
            writeErrorResponse(response, "Token đã hết hạn");
            return;
        } catch (SignatureException e) {
            writeErrorResponse(response, "Token không được tạo bởi hệ thống này");
            return;
        } catch (UnsupportedJwtException e) {
            writeErrorResponse(response, "Loại token không được hỗ trợ");
            return;
        } catch (Exception e) {
            writeErrorResponse(response, "Lỗi xác thực token");
            return;
        }
        filterChain.doFilter(request, response);

    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorResource error = new ErrorResource(message, null);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
