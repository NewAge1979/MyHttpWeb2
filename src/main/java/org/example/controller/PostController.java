package org.example.controller;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.NotFoundException;
import org.example.model.Post;
import org.example.service.PostService;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Controller
public class PostController {
    private static final Logger myLogger = LogManager.getLogger(PostController.class);
    private static final String APPLICATION_JSON = "application/json";
    private final Gson gson = new Gson();
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) {
        final var data = service.all();
        sendResponse(data, null, response);
    }

    public void getById(long id, HttpServletResponse response) throws NotFoundException {
        final var data = service.getById(id);
        sendResponse(null, data, response);
    }

    public void save(Reader body, HttpServletResponse response) throws NotFoundException {
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        sendResponse(null, data, response);
    }

    public void removeById(long id, HttpServletResponse response) throws NotFoundException {
        final var data = service.getById(id);
        service.removeById(id);
        sendResponse(null, data, response);
    }

    private void sendResponse(List<Post> dataList, Post dataPost, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        try {
            if (dataList == null) {
                response.getWriter().print(gson.toJson(dataPost));
            } else {
                response.getWriter().print(gson.toJson(dataList));
            }
        } catch (IOException e) {
            myLogger.error(e.getMessage());
        }
    }
}