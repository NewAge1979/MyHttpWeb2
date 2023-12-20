package org.example.servlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.PostConfig;
import org.example.controller.PostController;
import org.example.exception.NotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private final Logger myLogger = LogManager.getLogger(MainServlet.class);
    private enum Methods {GET, POST, DELETE}

    private final String POST = "/api/posts";
    private final String POST_ID = "/api/posts/\\d+";
    private PostController controller;

    @Override
    public void init() {
        final var springContext = new AnnotationConfigApplicationContext(PostConfig.class);
        controller = springContext.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        myLogger.info("Service");
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            long id;
            try {
                id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
            } catch (NumberFormatException e) {
                id = -1;
            }
            myLogger.info(String.format("Method: %s, path: %s, id: %d", method, path, id));
            switch (Methods.valueOf(method)) {
                case GET -> {
                    if (path.equals(POST)) {
                        controller.all(resp);
                    }
                    if (path.matches(POST_ID)) {
                        try {
                            controller.getById(id, resp);
                        } catch (NotFoundException e) {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        }
                    }
                }
                case POST -> {
                    if (path.equals(POST)) {
                        try {
                            controller.save(req.getReader(), resp);
                        } catch (NotFoundException e) {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        }
                    }
                }
                case DELETE -> {
                    if (path.matches(POST_ID)) {
                        try {
                            controller.removeById(id, resp);
                        } catch (NotFoundException e) {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        }
                    }
                }
                default -> resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            myLogger.error(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}