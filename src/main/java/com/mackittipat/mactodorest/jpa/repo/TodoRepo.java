package com.mackittipat.mactodorest.jpa.repo;

import com.mackittipat.mactodorest.jpa.entity.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "todos")
public interface TodoRepo extends CrudRepository<Todo, Long> {

}
