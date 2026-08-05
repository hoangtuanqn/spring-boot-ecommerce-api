package mst.local.mstsoftware.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mst.local.mstsoftware.helpers.IpHelper;
import mst.local.mstsoftware.resources.ApiResource;
import mst.local.mstsoftware.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final ProxyManager<String> proxyManager;

    private BucketConfiguration bucketConfig() {
        final int MAX_LIMIT_REQUEST = 20;
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.classic(MAX_LIMIT_REQUEST, Refill.greedy(MAX_LIMIT_REQUEST, Duration.ofMinutes(1))))
                .build();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = "rate_limit:" + IpHelper.getClientIp(request);

        var bucket = proxyManager.builder().build(key, this::bucketConfig);
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            writeErrorResponse(response, "Quá nhiều request, thử lại sau!");
            return;
        }
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        ErrorResource error = ErrorResource.builder()
                .code("TOO_MANY_REQUESTS")
                .build();
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResource.error(error, message)));
    }
}
