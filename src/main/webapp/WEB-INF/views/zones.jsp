<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>City Zones | SmartCity360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page wide">
    <p><a href="${pageContext.request.contextPath}/index.jsp">&larr; Back to dashboard</a></p>

    <div class="dash-head">
        <div>
            <h1>City Admin Dept: City Zones</h1>
            <p class="subtitle">One command, any level of the city.</p>
        </div>
        <c:if test="${not empty totalUsage}">
            <div class="big-kpi">
                <span class="kpi-label">City usage now</span>
                <span class="kpi-value"><fmt:formatNumber value="${totalUsage}" pattern="#,##0.0"/> <small>kWh</small></span>
            </div>
        </c:if>
    </div>

    <c:if test="${not empty message}">
        <div class="result"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <div class="layout">
        <div class="panel">
            <h2>Control panel</h2>
            <form method="post" action="${pageContext.request.contextPath}/zones">
                <label for="target">Zone or building</label>
                <select name="target" id="target">
                    <c:forEach var="n" items="${nodeNames}">
                        <option value="<c:out value='${n}'/>" ${n == selected ? 'selected' : ''}>
                            <c:out value="${n}"/>
                        </option>
                    </c:forEach>
                </select>

                <div class="btn-grid">
                    <button type="submit" name="action" value="report" class="btn-ghost">Report usage</button>
                    <button type="submit" name="action" value="shutdown" class="btn-danger">Shut down</button>
                    <button type="submit" name="action" value="restore" class="btn-ok">Restore</button>
                    <button type="submit" name="action" value="reset" class="btn-grey">Reset data</button>
                </div>
            </form>
            <p class="hint">The same four commands work on a building, a street, a district or the whole city.</p>
        </div>

        <div class="panel">
            <h2>Live city tree</h2>
            <pre class="tree-box"><c:out value="${treeText}"/></pre>
        </div>
    </div>
</div>
</body>
</html>