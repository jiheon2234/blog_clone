package com.blog.api.repository.post;

import java.util.List;

import com.blog.api.domain.Post;
import com.blog.api.request.post.PostSearch;

public interface PostRepositoryCustom {

	List<Post> getList(PostSearch postSearch);
}
