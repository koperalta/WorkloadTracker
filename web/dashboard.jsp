<%-- 
    Test Dashboard lang ito Boss Carl, Para lang ma-testing ko ang backend logic ko.Nalagay mo yong dashboard.jsp mo dito
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Dashboard Placeholder</title></head>
<body>
    <h1>Workload Dashboard</h1>
    <c:if test="${not empty workloadData}">
        <table border="1">
            <tr><th>Task Title</th><th>Status</th></tr>
            <c:forEach var="item" items="${workloadData}">
                <tr>
                    <td>${item.task.title}</td>
                    <td>${item.status}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${empty workloadData}">
        <p>No workload data found.</p>
    </c:if>
</body>
</html>