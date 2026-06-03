<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Новая заявка — Сервис онлайн запросов</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<nav>
    <a class="nav-brand" href="/">&#128196; Сервис онлайн запросов</a>
    <div class="nav-links">
        <span class="nav-user">
            <sec:authentication property="name"/>
        </span>
        <a href="/requests/my">Мои заявки</a>
        <a href="/requests/new" class="nav-active">Новая заявка</a>
        <form method="post" action="/logout" style="margin:0">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">Выйти</button>
        </form>
    </div>
</nav>

<div class="container">
    <div class="card">
        <div class="page-header">
            <h1>Новая заявка</h1>
        </div>

        <form method="post" action="/requests">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <label for="fullName">Полное имя</label>
                <input type="text" id="fullName" name="fullName"
                       placeholder="Иванов Иван Иванович" required>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email"
                       placeholder="example@mail.ru" required>
            </div>

            <div class="form-group">
                <label for="message">Текст обращения</label>
                <textarea id="message" name="message"
                          placeholder="Опишите вашу заявку подробно..." required></textarea>
            </div>

            <div style="display:flex; gap:.75rem; align-items:center">
                <button type="submit" class="btn btn-primary">Отправить заявку</button>
                <a href="/requests/my" class="btn btn-outline">Отмена</a>
            </div>
        </form>
    </div>
</div>

</body>
</html>
