<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Policy &amp; Law | SmartCity360</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page wide">
    <p><a href="${pageContext.request.contextPath}/index.jsp">&larr; Back to dashboard</a></p>
    <h1>Policy &amp; Law Dept: City Rules</h1>
    <p class="subtitle">Rules are plain text. Type one, test it, save it, all without restarting the server.</p>

    <c:if test="${not empty message}">
        <div class="result"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <div class="layout">
        <div class="panel">
            <h2>Situation and rule</h2>
            <form method="post" action="${pageContext.request.contextPath}/policies">
                <label for="noise">Noise (0-200 dB)</label>
                <input type="text" id="noise" name="noise" value="<c:out value="${empty param.noise ? 78 : param.noise}"/>">

                <label for="hour">Hour of day (0-23)</label>
                <input type="text" id="hour" name="hour" value="<c:out value="${empty param.hour ? 23 : param.hour}"/>">

                <label for="vehicle">Vehicle</label>
                <select name="vehicle" id="vehicle">
                    <c:forTokens items="CAR,TRUCK,BUS,MOTORBIKE,VAN" delims="," var="v">
                        <option value="${v}" ${(empty param.vehicle ? 'CAR' : param.vehicle) == v ? 'selected' : ''}>${v}</option>
                    </c:forTokens>
                </select>

                <label for="ruleText">Rule</label>
                <textarea id="ruleText" name="ruleText" rows="3"><c:out value="${empty param.ruleText ? 'NOISE > 70 AND TIME BETWEEN 22 AND 6' : param.ruleText}"/></textarea>

                <label for="ruleName">Name (only needed to save)</label>
                <input type="text" id="ruleName" name="ruleName" value="<c:out value="${param.ruleName}"/>">

                <div class="btn-grid">
                    <button type="submit" name="action" value="evaluate">Evaluate rule</button>
                    <button type="submit" name="action" value="add" class="btn-ok">Save rule</button>
                    <button type="submit" name="action" value="checkall" class="btn-ghost">Check all saved</button>
                </div>
            </form>
        </div>

        <div>
            <c:if test="${not empty evaluation}">
                <div class="panel" style="margin-bottom:16px">
                    <h2>Evaluation</h2>
                    <table>
                        <tr><th>Expression tree</th><td><code><c:out value="${evaluation.tree}"/></code></td></tr>
                        <tr><th>Situation</th><td><c:out value="${evaluation.situation}"/></td></tr>
                        <tr><th>Result</th>
                            <td>
                                <c:choose>
                                    <c:when test="${evaluation.triggered}"><span class="pill pill-trig">TRIGGERED</span></c:when>
                                    <c:otherwise><span class="pill pill-off">NOT TRIGGERED</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:if>

            <div class="panel">
                <h2>Saved city policies</h2>
                <table>
                    <tr><th>Name</th><th>Rule</th><th>Status</th><th></th></tr>
                    <c:forEach var="r" items="${rules}">
                        <tr>
                            <td><c:out value="${r.key}"/></td>
                            <td><code><c:out value="${r.value}"/></code></td>
                            <td>
                                <c:if test="${not empty checks}">
                                    <c:choose>
                                        <c:when test="${checks[r.key]}"><span class="pill pill-trig">TRIGGERED</span></c:when>
                                        <c:otherwise><span class="pill pill-off">not triggered</span></c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/policies" class="inline-form">
                                    <input type="hidden" name="name" value="<c:out value='${r.key}'/>">
                                    <button type="submit" name="action" value="remove" class="btn-danger">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <form method="post" action="${pageContext.request.contextPath}/policies" class="inline-form">
                    <button type="submit" name="action" value="reset" class="btn-grey" style="margin-top:12px">Reset sample rules</button>
                </form>
            </div>
        </div>
    </div>

    <div class="panel" style="margin-top:16px">
        <h2>Rule language</h2>
        <pre>NOISE &gt; 70                      noise above a number
TIME BETWEEN 22 AND 6            hour range (from inclusive, to exclusive, may wrap midnight)
VEHICLE = TRUCK                  vehicle type
NOT x    x AND y    x OR y       combine conditions   (NOT binds first, then AND, then OR)</pre>
    </div>
</div>
</body>
</html>