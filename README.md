# mactodorest

## Set Up
1. Clone project
2. Run `mvn clean install` 
3. Run `mvn spring-boot:run`

## Endpoints

http://localhost:8080/todos

* Insert : Use POST
* Update : Use PUT with `/todos/{id}`
* Delete : Use DELETE with `/todos/{id}`
* Search by title : http://localhost:8080/todos/search/findByTitle?title=T1
* Search by due date : http://localhost:8080/todos/search/findByDueDate?dueDate=20-02-2018 11:11
