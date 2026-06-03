<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Регистрация — Сервис онлайн запросов</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="login-wrap">
    <div class="login-card">

        <div class="login-logo">
            <div class="logo-icon">&#128196;</div>
            <h1>Сервис онлайн запросов</h1>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="/register">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <label for="username">Имя пользователя</label>
                <input type="text" id="username" name="username"
                       value="${username}" autofocus required>
            </div>

            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Повторите пароль</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>

            <button type="submit" class="btn btn-primary">Зарегистрироваться</button>
        </form>

        <p style="text-align:center; margin-top:16px;">
            Уже есть аккаунт? <a href="/login">Войти</a>
        </p>

    </div>
</div>
</body>
</html>
