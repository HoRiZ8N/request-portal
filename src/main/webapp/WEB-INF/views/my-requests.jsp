<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Мои заявки — Сервис онлайн запросов</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<nav>
    <a class="nav-brand" href="/">&#128196; Сервис онлайн запросов</a>
    <div class="nav-links">
        <span class="nav-user">
            <sec:authentication property="name"/>
        </span>
        <a href="/requests/my" class="nav-active">Мои заявки</a>
        <a href="/requests/new">Новая заявка</a>
        <form method="post" action="/logout" style="margin:0">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">Выйти</button>
        </form>
    </div>
</nav>

<div class="container">
    <div class="card">
        <div class="page-header">
            <h1>Мои заявки</h1>
            <a href="/requests/new" class="btn btn-primary">+ Новая заявка</a>
        </div>

        <c:if test="${param.submitted != null}">
            <div class="alert alert-success">Заявка успешно отправлена!</div>
        </c:if>

        <c:choose>
            <c:when test="${empty requests}">
                <div class="empty-state">
                    <div style="font-size:3rem">&#128203;</div>
                    <p>У вас пока нет заявок.</p>
                    <a href="/requests/new" class="btn btn-primary" style="margin-top:1rem">Подать первую заявку</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr>
                                <th class="col-id">#</th>
                                <th class="col-name">Имя</th>
                                <th class="col-email">Email</th>
                                <th class="col-msg">Обращение</th>
                                <th class="col-status">Статус</th>
                                <th class="col-date">Дата подачи</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="req" items="${requests}">
                                <tr>
                                    <td class="text-muted">${req.id}</td>
                                    <td>${req.fullName}</td>
                                    <td>${req.email}</td>
                                    <td><div class="msg-cell" title="${req.message}">${req.message}</div></td>
                                    <td>
                                        <span class="badge badge-${req.status}">
                                            <c:choose>
                                                <c:when test="${req.status == 'PENDING'}">На рассмотрении</c:when>
                                                <c:when test="${req.status == 'APPROVED'}">Одобрено</c:when>
                                                <c:otherwise>Отклонено</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td class="text-muted">${req.createdAtFormatted}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
