<%--
  Created by IntelliJ IDEA.
  User: abdullah
  Date: 2026-11-08
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title><%@ page contentType="text/html;charset=UTF-8" language="java" %>
           <%@ taglib prefix="c" uri="jakarta.tags.core" %>
           <!DOCTYPE html>
           <html lang="en">
           <head>
               <meta charset="UTF-8">
               <title>Energy Dept | SmartCity360</title>
               <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
           </head>
           <body>
           <div class="page wide">
               <p><a href="${pageContext.request.contextPath}/index.jsp">&larr; Back to dashboard</a></p>
               <h1>Energy Dept: Secure Meter Streams</h1>
               <p class="subtitle">Pick the layers to wrap around the stream, then send 20 meter readings through it.</p>
           
               <c:if test="${not empty error}">
                   <div class="error"><c:out value="${error}"/></div>
               </c:if>
           
               <div class="layout">
                   <div class="panel">
                       <h2>Build a stream</h2>
                       <form method="post" action="${pageContext.request.contextPath}/security">
                           <label class="check"><input type="checkbox" name="auth" value="on"
                               <c:if test="${empty param.action or not empty param.auth}">checked</c:if>> Authentication</label>
                           <label class="check"><input type="checkbox" name="compress" value="on"
                               <c:if test="${empty param.action or not empty param.compress}">checked</c:if>> Compression</label>
                           <label class="check"><input type="checkbox" name="encrypt" value="on"
                               <c:if test="${empty param.action or not empty param.encrypt}">checked</c:if>> Encryption</label>
           
                           <label for="apiKey">API key</label>
                           <input type="text" id="apiKey" name="apiKey"
                                  value="<c:out value="${empty param.action ? 'UT-SECRET-2026' : param.apiKey}"/>">
                           <p class="hint">Demo key: UT-SECRET-2026. Try a wrong one.</p>
           
                           <div class="btn-grid">
                               <button type="submit" name="action" value="send">Send readings</button>
                               <button type="submit" name="action" value="compare" class="btn-ghost">Compare order</button>
                           </div>
                       </form>
                   </div>
           
                   <div class="panel">
                       <h2>Result</h2>
           
                       <c:if test="${empty result and empty comparison}">
                           <p class="hint">Nothing sent yet.</p>
                       </c:if>
           
                       <c:if test="${not empty result}">
                           <table>
                               <tr><th>Layers used</th><td><c:out value="${result.layers}"/></td></tr>
                               <tr><th>Original size</th><td><c:out value="${result.originalLength}"/> characters</td></tr>
                               <tr><th>Final size</th><td><c:out value="${result.finalLength}"/> characters (<c:out value="${result.sizeChange}"/>)</td></tr>
                           </table>
                           <p class="hint">Start of the output:</p>
                           <pre><c:out value="${result.preview}"/></pre>
                       </c:if>
           
                       <c:if test="${not empty comparison}">
                           <table>
                               <tr><th>Original size</th><td><c:out value="${comparison.original}"/> characters</td></tr>
                               <tr><th>Compress, then encrypt (correct)</th><td><c:out value="${comparison.rightOrder}"/> characters</td></tr>
                               <tr><th>Encrypt, then compress (wrong)</th><td><c:out value="${comparison.wrongOrder}"/> characters</td></tr>
                           </table>
                           <p class="hint">The wrong order is <strong><c:out value="${comparison.ratio}"/>x</strong> bigger, because encrypted data looks random and cannot be compressed.</p>
                       </c:if>
                   </div>
               </div>
           </div>
           </body>
           </html></title>
  </head>
  <body>
  
  </body>
</html>
