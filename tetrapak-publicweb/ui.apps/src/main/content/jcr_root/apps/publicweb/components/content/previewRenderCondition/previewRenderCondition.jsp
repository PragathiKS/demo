<%--
  ADOBE CONFIDENTIAL

  Copyright 2015 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%><%@page session="false" import="com.adobe.granite.ui.components.Config,
                                    com.day.cq.wcm.api.Page,
                                    org.apache.sling.api.resource.Resource,
    								org.apache.sling.api.resource.ResourceResolver,
                                    com.adobe.granite.ui.components.rendercondition.RenderCondition,
                                    com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition,
    								javax.jcr.Session,java.util.List,
									com.adobe.granite.ui.components.Config,
									java.io.*,
									javax.servlet.*,
									javax.servlet.*,
    								javax.jcr.Node" %><%  
%><%@include file="/libs/granite/ui/global.jsp" %><% 


    // a condition to determine if the resource at the given path can be translate with CT

    boolean isPWSite = false;
    Config cfg = cmp.getConfig();
    String path = cmp.getExpressionHelper().getString(cfg.get("path", ""));

   if (path.startsWith("/content/tetrapak/publicweb") || path.startsWith("/content/online-help"))
   {
     isPWSite = true;
   }
 
    request.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(isPWSite));
%>