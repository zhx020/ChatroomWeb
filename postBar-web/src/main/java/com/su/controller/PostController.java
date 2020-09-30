package com.su.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.entity.Post;
import com.su.service.PostService;

@Controller
public class PostController {
	@Autowired
	private PostService postService;
	
	@RequestMapping(value="post",method= {RequestMethod.POST})
	public String addPost(HttpServletRequest request,String content,int roomId,String name) {
		postService.addPost(content,roomId,name);
		return "redirect:/post?roomId="+roomId;
	}
	@RequestMapping(value="post/{id}",method= {RequestMethod.DELETE})
	@ResponseBody
	public void deleteChatRoom(@PathVariable("id") int id) {
		postService.deletePost(id);
	}
	
	@RequestMapping(value="post/{id}",method= {RequestMethod.PUT})
	@ResponseBody
	public void updateChatRoom(HttpServletRequest request,@PathVariable("id") int id,
			String content,String name) {
		postService.updatePost(id,name,content);
	}
	
	@RequestMapping(value="post",method= {RequestMethod.GET})
	public String toChatRoom(HttpServletRequest request,
			Integer roomId,String begDate,String endDate) {
		List<Post> posts = postService.getPosts(roomId, begDate, endDate);
		request.setAttribute("posts", posts);
		return "post";
	}
	
	@RequestMapping(value="post/toUpdate/{id}",method= {RequestMethod.GET})
	public String toUpdate(HttpServletRequest request,@PathVariable("id") int id) {
		Post post = postService.getPost(id);
		request.setAttribute("post", post);
		return "updatePost";
	}
	
	@RequestMapping(value="post/toDeail/{id}",method= {RequestMethod.GET})
	public String toDeail(HttpServletRequest request,@PathVariable("id") int id) {
		Post post = postService.getPost(id);
		postService.recordClick(post);
		request.setAttribute("post", post);
		return "postDetail";
	}
	
	
}
