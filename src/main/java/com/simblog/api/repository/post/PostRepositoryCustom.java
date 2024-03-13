package com.simblog.api.repository.post;

import com.simblog.api.domain.Post;
import com.simblog.api.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}