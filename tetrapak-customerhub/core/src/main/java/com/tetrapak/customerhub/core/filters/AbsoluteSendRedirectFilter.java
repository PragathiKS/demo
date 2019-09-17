package com.tetrapak.customerhub.core.filters;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(
        immediate = true,
        service = Filter.class,
        property = {HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=/content/tetrapak/",
                HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_REGEX + "=/[a-z]*",
                HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=*)"
        })
public class AbsoluteSendRedirectFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //nothing to implement here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //continue the request
        chain.doFilter(request,
                new SendRedirectOverloadedResponse((HttpServletRequest) request, (HttpServletResponse) response).getResponse());
    }

    @Override
    public void destroy() {
        //nothing to implement here
    }
}
