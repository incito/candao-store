package com.candao.www.dataserver.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/15.
 */
public class MyHandlerMapping extends RequestMappingHandlerMapping {
    @Override
    protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
        ((AntPathMatcher) getPathMatcher()).setTrimTokens(false);
        return super.getMatchingMapping(info, request);
    }

    @Override
    protected void handleMatch(RequestMappingInfo info, String lookupPath, HttpServletRequest request) {
        super.handleMatch(info, lookupPath, request);
        Map<String, String> pathVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        for (Map.Entry<String,String> pathVar:pathVars.entrySet()){
            if(pathVar.getValue().trim().isEmpty()){
                pathVar.setValue("");
            }
        }
    }
}
