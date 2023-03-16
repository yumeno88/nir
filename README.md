# Приложение для отображения актуальных новостей дома (backend)

## Обзор

Приложение является RESTful сервисом, предоставляющим API для отображения актуальных новостей дома.

### На данном этапе сервис позволяет:

* добавить новость
* редактировать новость
* отобразить все новости
* отобразить конкретную новость
* отобразить отсортированные по тегам новости
* удалить новость
* выполнить CRUD операции над сущностями: тег, адрес, улица, район, подписка на новости

### Использованные технологии:

* Java 17
* Spring Boot
* PostgreSQL
* Hibernate
* Maven
* Lombok

### Посмотреть документацию

http://localhost:8080/swagger-ui/

## Схема БД

![drawSQL-nir-export-2023-02-10](https://user-images.githubusercontent.com/122719523/218102795-df109024-4eca-485f-a652-5d8ba9928fac.png)

## Эндпоинты для сущности новость

### Возваращает все новости

`GET /news`

#### Пример запроса

![image](https://user-images.githubusercontent.com/122719523/218102752-4a88f524-7ae8-428c-b601-1cccf0af7337.png)

#### Пример ответа

![image](https://user-images.githubusercontent.com/122719523/218103115-726cf9d4-0dc5-4d55-8f26-0324da7e263f.png)

### Возваращает новость по id

`GET /news/1`

#### Пример запроса

![image](https://user-images.githubusercontent.com/122719523/218103238-865368db-49a1-45cf-9320-d9e705e3e03a.png)

#### Пример ответа

![image](https://user-images.githubusercontent.com/122719523/218103310-f3f2f154-9385-46c4-9dd4-7ad814a86a4d.png)

### Возваращает все новости, отсортированные по тегам

`GET /news/sort?tags=gas,water`

#### Пример запроса

![image](https://user-images.githubusercontent.com/122719523/218103677-a3c23b59-9829-46a4-abe7-b6d7b7be859e.png)

#### Пример ответа

![image](https://user-images.githubusercontent.com/122719523/218103846-b21be343-3e57-4226-b385-bb3c364e4551.png)

### Добавляет новость

`POST /news`

#### Пример запроса

![image](https://user-images.githubusercontent.com/122719523/218104056-5d671f94-0863-44fd-ad4c-65d70b8c9668.png)

#### Пример ответа

![image](https://user-images.githubusercontent.com/122719523/218104137-fd051da2-930c-400b-bb61-33564cb25dc3.png)

### Изменяет новость

`PUT /news`

#### Пример запроса

![image](https://user-images.githubusercontent.com/122719523/218104278-b7846791-8ce7-4191-b8e8-f51581132d82.png)

#### Пример ответа

![image](https://user-images.githubusercontent.com/122719523/218104376-d4499f69-019d-46f8-8184-3b7c6d0a7d13.png)

### Удаляет новость по id

`DELETE /news/1`

#### Пример запроса

![image](https://user-images.githubusercontent.com/122719523/218104514-58ff03f0-607f-4dab-b365-71bd0dd9ecf6.png)

#### Пример ответа

![image](https://user-images.githubusercontent.com/122719523/218104557-74143f4e-dcc7-4720-a074-a1897b3eff93.png)
