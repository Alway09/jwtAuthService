# Сервис авторизации

-----------------------------------------------------
Данный проект является развитием собственного учебного проекта, оригинал: https://gitlab.com/Alway09/mini-bank/

-----------------------------------------------------
### Настройка проекта
Собственные настройки можно выставить в файле /src/main/resources/application.yaml<br>

* Конфигурация подключения к БД прописывается в параметр spring.datasource<br>
* Собственные секреты, на основе которых генерируются токены доступа, можно задать параметрами jwt.secret.access и jwt.secret.refresh.
Секреты должны быть закодированы по стандарту BASE64 и иметь длину более 256 символов.
-----------------------------------------------------
### Сборка проекта
Для сборки .jar файла проекта необходимо иметь следующее:
1. IDE с JDK 17 и Maven
2. PostgreSQL

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

#### 1.  Регистрация: POST http://localhost:8080/api/v1/client/signup

Тело запроса:
```
{
  "login":"string",
  "password":"string"
}
```

#### 2.  Аутентификация: POST http://localhost:8080/api/v1/auth/signin

Тело запроса:
```
{
  "login":"string",
  "password":"string"
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

#### 3.  Обновление данных: PUT http://localhost:8080/api/v1/client/update

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

Тело запроса:
```
{
  "newLogin":"string",
  "newPassword":"string"
}
```
Параметры "newLogin" и/или "newPassword" могут быть пустой строкой.

#### 4.  Остановка обслуживания: PATCH http://localhost:8080/api/v1/client/disable

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

#### 5.  Возобновление обслуживания: PATCH http://localhost:8080/api/v1/client/enable

Тело запроса:
```
{
  "login":"string",
  "password":"string"
}
```

#### 6.  Выпуск нового access токена: POST http://localhost:8080/api/v1/auth/token

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

#### 7.  Выпуск новых access и refresh токенов: POST http://localhost:8080/api/v1/auth/refresh

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

#### 8.  Выход из системы: POST http://localhost:8080/api/v1/auth/signout

Хедер:
```
 Имя: Authenticate
 Значение: Bearer JWT access token
```

#### 9.  Проверка статуса работы сервиса: GET http://localhost:8080/api/v1/health

В случае успешной проверки возвращается строка "ОК".