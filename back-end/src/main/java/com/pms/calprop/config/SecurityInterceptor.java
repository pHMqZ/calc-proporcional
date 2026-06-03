package com.pms.calprop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    public boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (request.getRequestURI().startsWith("/api")) {
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                return true;
            }

            String clientId = request.getHeader("X-Client-Id");

            if ("seeder-client".equals(clientId) && !activeProfile.contains("prod")) {
                return true;
            }

            if (clientId == null || !clientId.matches(UUID_REGEX)) {
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter()
                        .write("{\"message\": \"Sessão inválida ou expirada. Recarregue a página para continuar.\"}");

                return false;
            }

        }
        return true;
    }
}
