package com.ttn.bluebell.rest.services.common;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ttn.bluebell.durable.model.employee.Employee;
import com.ttn.bluebell.durable.model.event.notification.NotificationRequest;
import com.ttn.bluebell.rest.exception.AuthenticationFailureException;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ttn on 6/10/16.
 */

@RestController
@PropertySource("classpath:application.properties")
@ApiIgnore
public class AuthenticatorResource {

    @Resource
    private Environment env;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatorResource.class);

    @Autowired
    private AuthenticatorResourceService authenticatorResourceService;

    @RequestMapping(value = "/login/sfResponse",
            method = RequestMethod.GET
    )
    public void authenticatedUser(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam String status,
                                  @RequestParam String employeeCode,
                                  @RequestParam String email,
                                  @RequestParam(required = false) String practiceName,
                                  @RequestParam(required = false) String serviceLineName,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String uuid) throws IOException {
        try {

            logger.debug("Email: " + email);
            if (StringUtils.isEmpty(status) || StringUtils.isEmpty(employeeCode) || StringUtils.isEmpty(email) || !status.equalsIgnoreCase("success"))
                throw new AuthenticationFailureException("exception.authentication.failure");
            logger.debug("In login");
            response.sendRedirect(env.getProperty("client.redirect.base.url") + authenticatorResourceService.authenticated(request, employeeCode, email));
        } catch (AuthenticationFailureException | AuthorizationServiceException exp) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exp.getMessage());
        }
    }

    @RequestMapping(value = "/sso/callback",
            method = RequestMethod.GET
    )
    public void authenticatedUserCallBack(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam String access_token) throws IOException {

        try {
            String email = null;
            String employeeCode = null;
            Map<Integer, String> responseMap = getHTTPResponse(env.getProperty("oauth.redirect.user.url") + access_token);
            if (responseMap.containsKey(200)) {
                String userData = responseMap.get(200);
                logger.debug(userData);
                JsonObject jobj = new Gson().fromJson(userData, JsonObject.class);
                email = jobj.get("email").toString().replaceAll("\"", "");
                employeeCode = jobj.get("employeeCode").toString().replaceAll("\"", "");
                Cookie myCookie = new Cookie(env.getProperty("oauth.cookie.name"), access_token);
                myCookie.setDomain(env.getProperty("oauth.cookie.domain"));
                myCookie.setPath(request.getContextPath());
                response.addCookie(myCookie);
                redirectForAuthonetication(email, employeeCode, request, response);
            } else {
                logger.debug("Login Failed");
                response.sendRedirect(env.getProperty("oauth.redirect.authorize.url") + env.getProperty("oauth.client.id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(env.getProperty("oauth.redirect.authorize.url") + env.getProperty("oauth.client.id"));

        }

    }

    @RequestMapping(value = "/login/sso",
            method = RequestMethod.GET
    )
    public void authenticatedUserSsoLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String value = null;
            String email = null;
            String employeeCode = null;
            try {
                logger.debug(request.toString());
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (env.getProperty("oauth.cookie.name").equals(cookie.getName())) {
                            value = cookie.getValue();
                            logger.debug("Cookie found : " + value);
                            Map<Integer, String> responseMap = getHTTPResponse(env.getProperty("oauth.redirect.user.url") + value);
                            if (responseMap.containsKey(200)) {
                                String userData = responseMap.get(200);
                                logger.debug(userData);
                                logger.debug(responseMap.toString());
                                JsonObject jobj = new Gson().fromJson(userData, JsonObject.class);
                                email = jobj.get("email").toString().replaceAll("\"", "");
                                employeeCode = jobj.get("employeeCode").toString().replaceAll("\"", "");
                                redirectForAuthonetication(email, employeeCode, request, response);
                            } else {
                                logger.error("Auth Response: {}; Cookie is Invalid.", responseMap.toString());
                                response.sendRedirect(env.getProperty("oauth.redirect.authorize.url") + env.getProperty("oauth.client.id"));
                            }
                        }
                    }
                }
                if (StringUtils.isEmpty(value)) {
                    logger.debug("Cookie not found");
                    response.sendRedirect(env.getProperty("oauth.redirect.authorize.url") + env.getProperty("oauth.client.id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(env.getProperty("client.redirect.base.url") + authenticatorResourceService.authenticated(request, employeeCode, email));
            }
        } catch (AuthenticationFailureException | AuthorizationServiceException exp) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exp.getMessage());
            response.sendRedirect(env.getProperty("oauth.redirect.authorize.url") + env.getProperty("oauth.client.id"));
        }
    }

    private void redirectForAuthonetication(String email, String employeeCode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("Email: " + email);
        if (StringUtils.isEmpty(employeeCode) || StringUtils.isEmpty(email))
            throw new AuthenticationFailureException("exception.authentication.failure");
        logger.debug("In login");
        String token = authenticatorResourceService.authenticated(request, employeeCode, email);
        response.sendRedirect(env.getProperty("client.redirect.base.url") + token);
    }

    //To have a token generated without login
    @RequestMapping(value = "/login/sfResponse/imposter",
            method = RequestMethod.GET
    )
    public String authenticatedImposter(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam String email,
                                        @RequestParam String password) throws IOException {
        try {
//            Same prod email and password will be added
            if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password) || !password.equals(env.getProperty("api.password")) || !email.equalsIgnoreCase(env.getRequiredProperty("email.service.admin.name")))
                throw new AuthenticationFailureException("exception.authentication.failure");
            return authenticatorResourceService.authenticateImposter(request, email, email);
        } catch (AuthenticationFailureException exp) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exp.getMessage());
            return env.getProperty("exception.authentication.failure");
        }
    }

    @RequestMapping(value = "/login/sfResponse/validated",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String validateToken(HttpServletRequest request) {
        String requestAuthToken = request.getHeader("X-ACCESS-TOKEN");
        Element authentication = AuthenticatorResourceService.checkCache(requestAuthToken);
        if (StringUtils.isEmpty(requestAuthToken) || authentication == null || authentication.getObjectValue() == null)
            return "{\"result\":\"INVALID\"}";
        return "{\"result\":\"VALID\"}";
    }

    @RequestMapping(value = "/endSession",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        authenticatorResourceService.removeCookie(request,response);
        AuthenticatorResourceService.removeFromCache(request.getHeader("X-ACCESS-TOKEN"));
        return "{\"result\":\"SUCCESS\"}";
    }

    private Map<Integer,String> getHTTPResponse(String urlString)
    {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn=null;
        Map<Integer, String> responseMap = new HashMap<>();
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            responseMap.put(conn.getResponseCode(),result.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return  responseMap;
    }
    }
