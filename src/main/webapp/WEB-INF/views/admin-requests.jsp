<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Все заявки — Сервис онлайн запросов</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<nav>
    <a class="nav-brand" href="/">&#128196; Сервис онлайн запросов</a>
    <div class="nav-links">
        <span class="nav-user">
            <sec:authentication property="name"/> (admin)
        </span>
        <a href="/requests" class="${pendingView ? '' : 'nav-active'}">Все заявки</a>
        <a href="/requests/pending" class="${pendingView ? 'nav-active' : ''}">На рассмотрении</a>
        <a href="/h2-console" target="_blank">H2 Console</a>
        <form method="post" action="/logout" style="margin:0">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">Выйти</button>
        </form>
    </div>
</nav>

<div class="container">
    <div class="page-header" style="margin-bottom:1.25rem">
        <h1 style="font-size:1.4rem;color:#1e293b">
            <c:choose>
                <c:when test="${pendingView}">Заявки на рассмотрении</c:when>
                <c:otherwise>Все заявки</c:otherwise>
            </c:choose>
        </h1>
        <span class="text-muted">Всего: <strong>${page.totalElements}</strong></span>
    </div>

    <c:choose>
        <c:when test="${empty requests}">
            <div class="card">
                <div class="empty-state">
                    <div style="font-size:3rem">&#128203;</div>
                    <p>
                        <c:choose>
                            <c:when test="${pendingView}">Заявок на рассмотрении нет.</c:when>
                            <c:otherwise>Заявок пока нет.</c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="req-list">
                <c:forEach var="req" items="${requests}">
                    <div class="req-card">

                        <div class="req-card-head">
                            <div class="req-card-who">
                                <span class="req-name">${req.fullName}</span>
                                <span class="req-email">${req.email}</span>
                            </div>
                            <div class="req-card-meta">
                                <span class="badge badge-${req.status}">
                                    <c:choose>
                                        <c:when test="${req.status == 'PENDING'}">На рассмотрении</c:when>
                                        <c:when test="${req.status == 'APPROVED'}">Одобрено</c:when>
                                        <c:otherwise>Отклонено</c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="text-muted" style="font-size:.8rem">${req.createdAtFormatted}</span>
                            </div>
                        </div>

                        <div class="req-card-body">
                            <p class="text-muted" style="font-size:.8rem;margin:0 0 .35rem">
                                Подал: <strong>${req.submittedBy}</strong>
                            </p>
                            <p class="req-message">${req.message}</p>
                        </div>

                        <div class="req-card-foot">
                            <c:if test="${req.status != 'APPROVED'}">
                                <form method="post" action="/requests/${req.id}/approve" style="margin:0">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="returnTo" value="${baseUrl}?page=${page.number}"/>
                                    <button type="submit" class="btn btn-success btn-sm">&#10003; Одобрить</button>
                                </form>
                            </c:if>
                            <c:if test="${req.status != 'REJECTED'}">
                                <form method="post" action="/requests/${req.id}/reject" style="margin:0">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input type="hidden" name="returnTo" value="${baseUrl}?page=${page.number}"/>
                                    <button type="submit" class="btn btn-danger btn-sm">&#10007; Отклонить</button>
                                </form>
                            </c:if>
                        </div>

                    </div>
                </c:forEach>
            </div>

            <c:if test="${page.totalPages > 1}">
                <div class="pagination" style="display:flex;gap:.5rem;align-items:center;justify-content:center;margin-top:1.5rem">
                    <c:if test="${!page.first}">
                        <a class="btn btn-sm" href="${baseUrl}?page=${page.number - 1}">&#8592; Назад</a>
                    </c:if>
                    <span class="text-muted">Страница ${page.number + 1} из ${page.totalPages}</span>
                    <c:if test="${!page.last}">
                        <a class="btn btn-sm" href="${baseUrl}?page=${page.number + 1}">Вперёд &#8594;</a>
                    </c:if>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
