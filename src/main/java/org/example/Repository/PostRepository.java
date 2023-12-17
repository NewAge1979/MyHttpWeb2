package org.example.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Exception.NotFoundException;
import org.example.Model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private static final Logger myLogger = LogManager.getLogger(PostRepository.class);
    private final ConcurrentHashMap<Long, Post> allPosts = new ConcurrentHashMap<>();
    private final static AtomicLong lastId = new AtomicLong(0);

    public List<Post> all() {
        return allPosts.values().stream().toList();
    }

    public Optional<Post> getById(long idPost) {
        return Optional.ofNullable(allPosts.get(idPost));
    }

    public Post save(Post post) {
        var id = post.getId();
        if (id == 0 || allPosts.get(id) != null) {
            if (id == 0) {
                id = lastId.incrementAndGet();
                post.setId(id);
            }
            allPosts.put(id, post);
            myLogger.info(String.format("Add/Edit post: %d", id));
        } else {
            throw new NotFoundException();
        }
        return post;
    }

    public void removeById(long id) {
        if (allPosts.get(id) != null) {
            allPosts.remove(id);
            myLogger.info(String.format("Delete post: %d", id));
        } else {
            throw new NotFoundException();
        }
    }
}