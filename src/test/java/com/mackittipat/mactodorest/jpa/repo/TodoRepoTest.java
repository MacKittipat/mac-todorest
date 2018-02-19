package com.mackittipat.mactodorest.jpa.repo;

import org.junit.Test;

import static io.restassured.RestAssured.when;

public class TodoRepoTest {

    @Test
    public void test() {
        when().get("/todos/").then().statusCode(200);
    }
}