# Сервис авторизации

-----------------------------------------------------
Данный проект является развитием собственного учебного проекта, оригинал: https://gitlab.com/Alway09/mini-bank/

-----------------------------------------------------
### Стэк
Spring Boot 3 (Security, DataJPA, Web, Validation), Hibernate, JUnit5, Maven, PostgreSQL, H2, Docker, OpenAPI, JSON Web Token

-----------------------------------------------------
### Настройка проекта
Собственные настройки можно выставить в файле /src/main/resources/application.yaml<br>

* Конфигурация подключения к БД прописывается в параметр spring.datasource<br>
* Собственные секреты, на основе которых генерируются токены доступа, можно задать параметрами jwt.secret.access и jwt.secret.refresh.
Секреты должны быть закодированы по стандарту BASE64 и иметь длину более 256 символов.
* Если инициализировать БД нет необходимости, можно поставить параметр spring.sql.init.mode в значение "never".
-----------------------------------------------------
### Сборка проекта
Для сборки .jar файла проекта необходимо иметь следующее:
1. IDE с JDK 17 и Maven

Этапы сборки:
1. Открыть проект в IDE, дождаться пока Maven загрузит зависимости
2. Запустить фазу "package" Maven
3. Дождаться сборки проекта

Для запуска проекта в Docker необходимо:
1. Собрать .jar файл проекта
2. В папке "docker/scripts" запустить скрипт "create-image.sh" (создание Docker образа)
3. В этой же папке запустить скрипт "up-image.sh"
-----------------------------------------------------
### Описание API
[OpenAPI REST API documentation](http://localhost:8080/swagger-ui/index.html)

1. ### Регистрация клиента: POST http://localhost:8080/api/v1/client/signup

Хедер:
```
 Имя: Authenticate
 Значение: Basic Base64(login:password)
```

2. ### Аутентификация клиента: POST http://localhost:8080/api/v1/auth/signin

Хедер:
```
 Имя: Authenticate
 Значение: Basic Base64(login:password)
```

Ответ:
```
{
  "type":"Bearer",
  "accessToken":"string",
  "refreshToken":"string"
}
```

3. ### Обновление данных клиента: PUT http://localhost:8080/api/v1/client/update

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

Тело запроса:
```
{
  "newLogin":"string"
}
```

4. ### Обновление пароля клиента: PUT http://localhost:8080/api/v1/client/changePass

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

Тело запроса:
```
{
  "newPassword":"string"
}
```

5. ### Остановка обслуживания клиента: PATCH http://localhost:8080/api/v1/client/disable

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

6. ### Возобновление обслуживания клиента: PATCH http://localhost:8080/api/v1/client/enable

Хедер:
```
 Имя: Authenticate
 Значение: Basic Base64(login:password)
```

7. ### Выпуск нового access токена: POST http://localhost:8080/api/v1/auth/token

Тело запроса:
```
{
  "refreshToken":"string"
}
```

Ответ:
```
{
  "type":"Bearer",
  "accessToken":"string",
  "refreshToken":"string"
}
```

8. ### Выпуск новых access и refresh токенов: POST http://localhost:8080/api/v1/auth/refresh

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

Тело запроса:
```
{
  "refreshToken":"string"
}
```

Ответ:
```
{
  "type":"Bearer",
  "accessToken":"string",
  "refreshToken":"string"
}
```

9. ### Выход из системы: POST http://localhost:8080/api/v1/auth/signout

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

10. ### Проверка статуса работы микросервиса: GET http://localhost:8080/api/v1/health

В случае успешной проверки возвращается строка "ОК".