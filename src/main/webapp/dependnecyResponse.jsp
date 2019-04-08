<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.Charset" %>
<c:set var="encoding" value="<%= Charset.defaultCharset().name() %>" />
<html>
<head>
    <title>Dependency Response - ${URLDecoder.decode(param.url, encoding)}</title>
</head>
<body>
<h1>Response</h1>
<p>Sent with <strong>${httpClient}</strong></p>
<ul>
    <li>URL: ${URLDecoder.decode(dependencyUrl, encoding)}</li>
    <li>Status: ${dependencyStatusLine.getStatusCode()} ${dependencyStatusLine.getReasonPhrase()}</li>
    <li>TTR: <fmt:formatNumber type="number" maxFractionDigits="3" minFractionDigits="3" value="${dependencyTtr / 1000.0}" /> sec</li>
    <li>Content Length: <fmt:formatNumber value="${dependencyContentLength}" /> bytes</li>
</ul>
<jsp:include page="footer.jsp" />
</body>
</html>
