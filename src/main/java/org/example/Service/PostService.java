package org.example.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Controller.PostController;
import org.example.Exception.NotFoundException;
import org.example.Model.Post;
import org.example.Repository.PostRepository;

import java.util.List;

public class PostService {
    private static final Logger myLogger = LogManager.getLogger(PostService.class);
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id).orElseThrow(NotFoundException::new);
    }

    public Post save(Post post) throws NotFoundException {
        return repository.save(post);
    }

    public void removeById(long id) throws NotFoundException {
        repository.removeById(id);
    }
}