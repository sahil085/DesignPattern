package com.ttn.bluebell.security;

import com.ttn.bluebell.rest.exception.AuthenticationFailureException;
import com.ttn.bluebell.rest.exception.CustomisedExceptionMessageLoader;
import com.ttn.bluebell.rest.services.common.AuthenticatorResourceService;
import net.sf.ehcache.Element;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessAuthenticationFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StatelessAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (!allowURL(request.getRequestURL())) {
                String requestAuthToken = request.getHeader("X-ACCESS-TOKEN");
                Element authentication = AuthenticatorResourceService.checkCache(requestAuthToken);
                if (StringUtils.isEmpty(requestAuthToken) || authentication == null || authentication.getObjectValue() == null)
                    throw new AuthenticationFailureException(CustomisedExceptionMessageLoader.getPropertyValue("exception.authentication.failure"));
                else
                    SecurityContextHolder.getContext().setAuthentication((PreAuthenticatedAuthenticationToken) authentication.getObjectValue());
            }
            chain.doFilter(request, response);
        } catch (AuthenticationFailureException authEx) {
            logger.error("ErrorMessage = {} ", authEx.getMessage(), authEx);
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authEx.getMessage());
        }
    }

    public boolean allowURL(StringBuffer requestURL) {
        if (StringUtils.isEmpty(requestURL))
            return false;
        String url = requestURL.toString();
        if (url.contains("/login/sfResponse"))
            return true;
        if (url.contains("/refreshCache"))
            return true;
        else if (url.contains(".html"))
            return true;
        else if (url.contains(".png"))
            return true;
        else if (url.contains(".css"))
            return true;
        else if (url.contains(".js"))
            return true;
        else if (url.contains("jpg"))
            return true;
        else if (url.contains("jpeg"))
            return true;
        else if (url.contains("bmp"))
            return true;
        else if (url.contains("gif"))
            return true;
        else if (url.contains("swagger-resources"))
            return true;
        else if (url.contains("/v2/api-docs"))
            return true;
        else if (url.contains("/login/sso"))
            return true;
        else if (url.contains("/sso/callback"))
            return true;
        return false;
    }
}
