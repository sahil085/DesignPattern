package com.ttn.bluebell.rest.services.common;

import com.ttn.bluebell.domain.employee.EEmployeeRoles;
import com.ttn.bluebell.durable.model.employee.Role;
import com.ttn.bluebell.repository.config.EmployeeRoleRepository;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by ttn on 10/10/16.
 */

@Service
public class AuthenticatorResourceService {
    @Resource
    private Environment env;
    @Autowired
    EmployeeRoleRepository employeeRoleRepository;

    private static Cache cache = CacheManager.newInstance().getCache("loggedIn");

    public String authenticated(HttpServletRequest request, String employeeCode, String email) throws AuthorizationServiceException {

        List<EEmployeeRoles> employeeRoles = employeeRoleRepository.findByEmail(email);
        if (employeeRoles != null && !employeeRoles.isEmpty()) {
            String roleSet = employeeRoles.stream().map(role -> role.getRole()).collect(Collectors.joining(","));
            String[] roles = StringUtils.commaDelimitedListToStringArray(roleSet);
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(employeeCode, email, authorities);
            if (request != null)
                authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return prepareCache(UUID.randomUUID().toString(), authentication);
        } else
            throw new AuthorizationServiceException("exception.authorization.failure");
    }

    public String authenticateImposter(HttpServletRequest request, String employeeCode, String email) throws AuthorizationServiceException {
        Role[] roles = Role.values();
        String[] roleNames = new String[roles.length];
        for (int i = 0; i < roles.length; i++)
            roleNames[i] = roles[i].getName();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleNames);
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(employeeCode, email, authorities);
        if (request != null)
            authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return prepareCache(UUID.randomUUID().toString(), authentication);
    }

    private String prepareCache(String authToken, PreAuthenticatedAuthenticationToken authentication) {
        cache.putIfAbsent(new Element(authToken, authentication));
        return authToken;
    }

    public static Element checkCache(String authToken) {
        return cache.get(authToken);
    }

    public static void removeFromCache(String authToken) {
        cache.remove(authToken);
    }

    public void removeCookie(HttpServletRequest request,HttpServletResponse response) {
        Cookie myCookie =
                new Cookie(env.getProperty("oauth.cookie.name"), null);
        myCookie.setDomain(env.getProperty("oauth.cookie.domain"));
        myCookie.setPath(request.getContextPath());
        myCookie.setMaxAge(0);
        response.addCookie(myCookie);

    }
}