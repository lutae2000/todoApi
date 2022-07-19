package com.example.todoapi.todoTest;

import com.example.todoapi.TodoApiApplication;
import com.sun.net.httpserver.HttpHandler;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.charset.Charset;
import java.util.Base64;


@SpringBootTest(classes = TodoApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerIT {
    @Autowired
    private TestRestTemplate template;

    HttpHeaders headers = createHeaders("user-name", "user-password");

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    @Test
    public void retrieveTodo() throws Exception{
        String expected = "[{id:1, user:Jack, desc:\"Learn Spring MVC\", done:false}" +
                            ",{id:2, user:Jack, desc:\"Learn Struts\", done:false}]";

        ResponseEntity<String> response = template.exchange(
                "/users/Jack/todos",
                HttpMethod.GET,
                new HttpEntity<String>(null, headers), String.class);
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
}
