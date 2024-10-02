package com.ndp.token;

import com.google.gson.Gson;
import com.ndp.config.UrlWhitelistConfig;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UrlWhitelistConfig urlWhitelistConfig;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return urlWhitelistConfig
                .getUrls()
                .stream()
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Entering JWT filter for {}", request.getServletPath());

        try {
            String token = TokenUtil.getToken(request);
            if (StringUtils.isEmpty(token)) {
                log.info("No JWT token found in request headers or cookies");
                response.sendRedirect("/login");
                return;
            }

            String username = jwtService.getUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            sendErrorMessage(response, ex);
        }
    }

    private void sendErrorMessage(HttpServletResponse response, Exception exception)
            throws IOException {
        HttpStatus errStatus = HttpStatus.UNAUTHORIZED;

        Map<String, Object> err = new HashMap<>();
        err.put("timestamp", new Date());
        err.put("status", errStatus.value());
        err.put("error", exception.getMessage());

        ResponseDto error = new ResponseDto();
        error.setCode(errStatus.value());
        error.setStatus(errStatus.getReasonPhrase());
        error.setData(err);

        response.setStatus(errStatus.value());
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(error));
    }
}
