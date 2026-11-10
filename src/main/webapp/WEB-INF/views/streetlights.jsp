<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lighting Dept | SmartCity360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page wide">
    <p><a href="${pageContext.request.contextPath}/index.jsp">&larr; Back to dashboard</a></p>
    <h1>Lighting Dept: Streetlights</h1>
    <p class="subtitle">Thousands of lights, only a handful of shared configurations.</p>

    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <div class="layout">
        <div class="panel">
            <h2>Memory benchmark</h2>
            <form method="post" action="${pageContext.request.contextPath}/streetlights">
                <label for="count">Number of streetlights</label>
                <input type="number" name="count" id="count" min="1" max="200000"
                       value="<c:out value="${empty param.count ? 10000 : param.count}"/>">
                <button type="submit" name="action" value="run">Run benchmark</button>
            </form>
            <p class="hint">Enter 1 to 200,000 lights. Builds them twice (with and without the Flyweight) and measures the memory used. Large numbers can take a few seconds. Figures are approximate (garbage collection adds noise), so run it a few times.</p>
        </div>

        <div class="panel">
            <h2>Result</h2>

            <c:if test="${empty result}">
                <p class="hint">Nothing measured yet.</p>
            </c:if>

            <c:if test="${not empty result}">
                <table>
                    <tr><th></th><th>With Flyweight</th><th>Without Flyweight</th></tr>
                    <tr>
                        <th>Streetlight objects</th>
                        <td><fmt:formatNumber value="${result.count}" pattern="#,##0"/></td>
                        <td><fmt:formatNumber value="${result.count}" pattern="#,##0"/></td>
                    </tr>
                    <tr>
                        <th>Shared LightType objects</th>
                        <td><c:out value="${result.poolSize}"/></td>
                        <td>0 (every light holds its own copy)</td>
                    </tr>
                    <tr>
                        <th>Memory used</th>
                        <td><fmt:formatNumber value="${result.flyMb}" pattern="0.00"/> MB</td>
                        <td><fmt:formatNumber value="${result.plainMb}" pattern="0.00"/> MB</td>
                    </tr>
                    <tr>
                        <th>Approx. bytes per light</th>
                        <td><fmt:formatNumber value="${result.flyPerLight}" pattern="#,##0"/></td>
                        <td><fmt:formatNumber value="${result.plainPerLight}" pattern="#,##0"/></td>
                    </tr>
                </table>
                <p class="hint">
                    Median of <c:out value="${result.runsUsed}"/> valid runs.
                    Range with Flyweight: <c:out value="${result.flyRangeMb}"/>;
                    without: <c:out value="${result.plainRangeMb}"/>.
                </p>
                <div class="result">
                    Memory saved: <strong><fmt:formatNumber value="${result.savedPercent}" pattern="0"/>%</strong>
                    (<fmt:formatNumber value="${result.plainMb - result.flyMb}" pattern="0.00"/> MB)
                </div>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty preview}">
        <div class="panel" style="margin-top:16px">
            <h2>First five lights</h2>
            <table>
                <tr><th>ID</th><th>Location</th><th>Status</th><th>Model</th><th>Watts</th><th>Bulb</th><th>Brightness</th></tr>
                <c:forEach var="l" items="${preview}">
                    <tr>
                        <td><c:out value="${l.id}"/></td>
                        <td><c:out value="${l.location}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${l.on}"><span class="pill pill-on">ON</span></c:when>
                                <c:otherwise><span class="pill pill-off">OFF</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${l.type.model}"/></td>
                        <td><c:out value="${l.type.wattage}"/></td>
                        <td><c:out value="${l.type.bulbType}"/></td>
                        <td><c:out value="${l.type.brightness}"/></td>
                    </tr>
                </c:forEach>
            </table>
            <p class="hint">The first five lights use 3 types. Lights 1 and 4 point at the <strong>same</strong> LightType object in memory. The first three columns are unique per light, the last four are shared.</p>
        </div>
    </c:if>
</div>
</body>
</html>

