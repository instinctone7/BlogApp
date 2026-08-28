package com.InstinctOne.BlogApp.security;

import com.InstinctOne.BlogApp.dtos.ErrorResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token Header Error", request.getRequestURI());
            return;
        }

        String jwt = token.substring(7);
        try {
            Claims claims = JwtUtil.validateToken(jwt);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: " + e.getMessage(), request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, int status, String message, String path) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(ZoneId.of("UTC")),
                status,
                status == HttpServletResponse.SC_UNAUTHORIZED ? "Unauthorized" : "Forbidden",
                message,
                path
        );

        response.getWriter().write(toJson(body));
    }

    private String toJson(ErrorResponse body) {
        return "{" +
                "\"timestamp\":\"" + escapeJson(body.timestamp().toString()) + "\"," +
                "\"status\":" + body.status() + "," +
                "\"error\":\"" + escapeJson(body.error()) + "\"," +
                "\"message\":\"" + escapeJson(body.message()) + "\"," +
                "\"path\":\"" + escapeJson(body.path()) + "\"" +
                "}";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/register")
                || path.equals("/api/auth/signIn")
                || path.equals("/api/auth/verification")
                || path.equals("/api/auth/about");
    }
}
