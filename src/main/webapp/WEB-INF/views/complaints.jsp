<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Citizen Services | SmartCity360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page wide">
    <p><a href="${pageContext.request.contextPath}/index.jsp">&larr; Back to dashboard</a></p>
    <h1>Citizen Services: Complaints</h1>
    <p class="subtitle">Severity 1-2 &rarr; Local Staff &nbsp;|&nbsp; 3-4 &rarr; Supervisor &nbsp;|&nbsp; 5 &rarr; City Manager</p>

    <c:if test="${not empty message}">
        <div class="result"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <div class="layout">
        <div class="panel">
            <h2>Submit a complaint</h2>
            <form method="post" action="${pageContext.request.contextPath}/complaints">
                <label for="citizen">Citizen name</label>
                <input type="text" id="citizen" name="citizen"
                       value="<c:if test="${not empty error}"><c:out value="${param.citizen}"/></c:if>">

                <label for="description">Description</label>
                <input type="text" id="description" name="description"
                       value="<c:if test="${not empty error}"><c:out value="${param.description}"/></c:if>">

                <label for="severity">Severity (1 = minor, 5 = critical)</label>
                <input type="text" id="severity" name="severity"
                       value="<c:if test="${not empty error}"><c:out value="${param.severity}"/></c:if>">

                <div class="btn-grid">
                    <button type="submit" name="action" value="submit">Submit</button>
                    <button type="submit" name="action" value="reset" class="btn-grey">Reset data</button>
                </div>
            </form>
        </div>

        <div class="panel">
            <h2>All complaints</h2>
            <table>
                <tr>
                    <th>ID</th><th>Citizen</th><th>Description</th>
                    <th>Severity</th><th>Path</th><th>Handled by</th>
                </tr>
                <c:forEach var="item" items="${complaints}">
                    <tr>
                        <td><c:out value="${item.id}"/></td>
                        <td><c:out value="${item.citizen}"/></td>
                        <td><c:out value="${item.description}"/></td>
                        <td><c:out value="${item.severity}"/></td>
                        <td><c:out value="${item.pathText}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${item.handled}"><span class="pill pill-on"><c:out value="${item.handledBy}"/></span></c:when>
                                <c:otherwise><span class="pill pill-unhandled">UNHANDLED</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" class="log-cell">
                            <details>
                                <summary>Trace log</summary>
                                <c:forEach var="line" items="${item.log}">
                                    <div class="log-line"><c:out value="${line}"/></div>
                                </c:forEach>
                            </details>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
</body>
</html>