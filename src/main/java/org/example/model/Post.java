package org.example.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
public class Post {
    private long id;
    private String content;

    public Post() {

    }

    public Post(long id, String content) {
        this.id = id;
        this.content = content;
    }

}