<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SmartCity360 | UrbanTech Solutions</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <h1>SmartCity360</h1>
    <p class="subtitle">UrbanTech Solutions: Colombo Smart City Platform</p>

    <div class="cards">
        <a class="card" href="${pageContext.request.contextPath}/security">
            <h2>Energy Dept</h2>
            <p>Secure meter data streams</p>
            <span class="tag">Part A</span>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/zones">
            <h2>City Admin Dept</h2>
            <p>City zones and power control</p>
            <span class="tag">Part B</span>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/complaints">
            <h2>Citizen Services</h2>
            <p>Complaint escalation</p>
            <span class="tag">Part C</span>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/streetlights">
            <h2>Lighting Dept</h2>
            <p>Streetlight memory efficiency</p>
            <span class="tag">Part D</span>
        </a>
        <a class="card" href="${pageContext.request.contextPath}/policies">
            <h2>Policy &amp; Law Dept</h2>
            <p>City rules as a mini-language</p>
            <span class="tag">Part E</span>
        </a>
    </div>
</div>
</body>
</html>