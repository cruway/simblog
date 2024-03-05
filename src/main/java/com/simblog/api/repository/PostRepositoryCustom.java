package com.simblog.api.repository;

import com.simblog.api.domain.Post;
import com.simblog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}