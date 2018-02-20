package com.mackittipat.mactodorest.jpa.repo;

import com.mackittipat.mactodorest.jpa.entity.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@RepositoryRestResource(path = "todos")
public interface TodoRepo extends CrudRepository<Todo, Long> {

    Iterable<Todo> findByTitle(@Param("title") String title);

    Iterable<Todo> findByDueDate(@Param("dueDate")
                                 @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime dueDate);
}
