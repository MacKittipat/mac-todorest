# mactodorest

## Requirement
* Java 8
* Maven
* **No database is required. I use H2, in-memory database**

## Set Up

1. Clone project and `cd` to project directory
2. Run `mvn clean install` 
3. Run `mvn spring-boot:run`

## Endpoints

http://localhost:8080/todos

* Insert : Send `POST` request with below JSON Body

```
{
	"title": "Title1",
	"desc": "Desc1",
	"dueDate": "20-02-2018 11:11"
}
```

* Update : Send `PUT` request to `/todos/{id}` with below JSON Body

```
{
	"title": "Title2",
	"desc": "Desc1",
	"dueDate": "20-02-2018 11:11"
}
```

* Delete : Send `DELETE` request to `/todos/{id}`

* Search by title : Send `GET` to http://localhost:8080/todos/search/findByTitle?title=T1

* Search by due date : Send `GET` to http://localhost:8080/todos/search/findByDueDate?dueDate=20-02-2018 11:11

## Test with Postman

Import https://github.com/MacKittipat/mactodorest/blob/master/Todo.postman_collection.json to Postman app.
