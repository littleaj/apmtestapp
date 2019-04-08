<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.nio.charset.Charset" %>

<c:set var="encoding" value="<%= Charset.defaultCharset().name() %>" />
<c:set var="uri" value="${URLDecoder.decode(requestScope['javax.servlet.forward.request_uri'], encoding)}" />
<p>
    Current page: ${uri}<br />
    <a href="./">Home</a> | <a href="./configure">Configure</a>
</p>