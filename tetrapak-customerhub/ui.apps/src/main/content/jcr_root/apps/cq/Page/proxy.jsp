<%@page session="false"
            import="org.apache.sling.api.request.RequestDispatcherOptions,
                    org.apache.sling.api.resource.Resource,
                    org.apache.sling.api.resource.ValueMap,
                    org.slf4j.Logger,
                    org.slf4j.LoggerFactory,
                    com.day.cq.wcm.api.NameConstants"
%><%@ taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %><%!

    private final Logger logger = LoggerFactory.getLogger(getClass());

%><sling:defineObjects /><%

    Resource cr = resourceResolver.getResource(resource, NameConstants.NN_CONTENT);
    if (cr == null) {
        logger.error("resource has no content. path={} referrer={}", request.getRequestURI(), request.getHeader("Referrer"));
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "no content");
        return;
    }

    RequestDispatcherOptions options = new RequestDispatcherOptions();
    ValueMap crValueMap = cr.getValueMap();
    RequestDispatcher crd;
    if(crValueMap != null && crValueMap.containsKey(NameConstants.PN_REDIRECT_TARGET) && slingRequest.getRequestPathInfo().getSelectors().length == 0) {
        options.setAddSelectors("redirect");
        crd = slingRequest.getRequestDispatcher(resource, options);
    } else if(crValueMap != null && crValueMap.containsKey(NameConstants.PN_REDIRECT_TARGET) && slingRequest.getRequestPathInfo().getSelectors().length == 1) {
        options.setReplaceSelectors("redirect");
        crd = slingRequest.getRequestDispatcher(resource, options);
    } else {
        crd = slingRequest.getRequestDispatcher(cr, options);
    }
    if (crd != null) {
        crd.include(request, response);
        return;
    } else {
        logger.error("Unable to dispatch proxy request.for {} referrer={}", request.getRequestURI(), request.getHeader("Referrer"));
        throw new ServletException("No Content");
    }
%>