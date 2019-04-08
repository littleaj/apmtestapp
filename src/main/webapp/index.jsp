<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>${domain.name} Test App</title>
</head>
<body>
<h1>Test App</h1>
<h2>${domain.name}</h2>
<h3>Http Client Request</h3>
<form method="get" action="http">
    <label for="url-text">URL: </label><input name="url" value="www.microsoft.com" id="url-text" />
    <input type="submit" value="Send Request" />
</form>
<jsp:include page="footer.jsp" />
</body>
</html>
