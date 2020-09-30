package com.su.service;

import java.util.List;
import com.su.entity.Post;

public interface PostService {

	void addPost(String content,int roomId,String name);

	void deletePost(int id);

	void updatePost(int id, String name,String content);

	List<Post> getPosts(Integer roomId,String begDate,String endDate);

	Post getPost(int id);

	void recordClick(Post post);

}
