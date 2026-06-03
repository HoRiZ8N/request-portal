<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Вход — Сервис онлайн запросов</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="login-wrap">
    <div class="login-card">

        <div class="login-logo">
            <div class="logo-icon">&#128196;</div>
            <h1>Сервис онлайн запросов</h1>
        </div>

        <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-error">Неверное имя пользователя или пароль.</div>
        <% } %>
        <% if (request.getParameter("logout") != null) { %>
        <div class="alert alert-success">Вы вышли из системы.</div>
        <% } %>
        <% if (request.getParameter("registered") != null) { %>
        <div class="alert alert-success">Регистрация прошла успешно. Войдите в систему.</div>
        <% } %>

        <form method="post" action="/login">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <label for="username">Имя пользователя</label>
                <input type="text" id="username" name="username" autofocus required>
            </div>

            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" required>
            </div>

            <button type="submit" class="btn btn-primary">Войти</button>
        </form>

        <p style="text-align:center; margin-top:16px;">
            Нет аккаунта? <a href="/register">Зарегистрироваться</a>
        </p>

    </div>
</div>
</body>
</html>
