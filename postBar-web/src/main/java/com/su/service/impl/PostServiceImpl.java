package com.su.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.annotations.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.base.constant.DateConstant;
import com.su.base.util.DateUtil;
import com.su.dao.PostClickDao;
import com.su.dao.PostDao;
import com.su.entity.ChatRoom;
import com.su.entity.Post;
import com.su.entity.PostClick;
import com.su.service.ChatRoomService;
import com.su.service.PostService;

@Service
public class PostServiceImpl implements PostService{
	@Autowired
	private PostDao postDao; 
	@Autowired
	private PostClickDao postClickDao; 
	@Autowired
	private ChatRoomService chatRoomService;
	
	@Override
	public void addPost(String content,int roomId,String name) {
		Post post = new Post();
		post.setContent(content);
		post.setName(name);
		post.setRoomId(roomId);
		post.setPublishDate(DateUtil.dateConvertStr(new Date(), DateConstant.Y_M_D));
		ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);
		chatRoom.addPostCount();
		postDao.save(post);
	}

	@Override
	public void deletePost(int id) {
		Post post = postDao.get(Post.class, id);
		ChatRoom chatRoom = chatRoomService.getChatRoom(post.getRoomId());
		chatRoom.subPostCount();
		post.setIsDelete(1);
	}

	@Override
	public void updatePost(int id, String name,String content) {
		Post post = postDao.get(Post.class, id);
		post.setName(name);
		post.setContent(content);
	}

	@Override
	public List<Post> getPosts(Integer roomId, String begDate, String endDate) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("from " + Post.class.getSimpleName() + " where isDelete = 0");
		if(roomId != null) {
			sql.append("and roomId = ?");
			params.add(roomId);
		}
		if(StringUtils.isNoneBlank(begDate)) {
			sql.append("and roomId = ?");
			params.add(roomId);
		}
		if(StringUtils.isNoneBlank(endDate)) {
			sql.append("and roomId = ?");
			params.add(endDate);
		}
		List<Post> queryList = postDao.queryList(sql.toString(), params);
		if(CollectionUtils.isNotEmpty(queryList)) {
			for(Post post : queryList) {
				post.setRoomName(chatRoomService.getChatRoom(post.getRoomId()).getName());
			}
		}
		return queryList;
	}

	@Override
	public Post getPost(int id) {
		Post post = postDao.get(Post.class, id);
		ChatRoom chatRoom = chatRoomService.getChatRoom(post.getRoomId());
		post.setRoomName(chatRoom.getName());
		return post;
	}

	@Override
	public void recordClick(Post post) {
		post.addClickTimes();
		postDao.update(post);
		PostClick postClick = new PostClick();
		postClick.setRoomId(post.getRoomId());
		postClick.setDate(DateUtil.dateConvertStr(new Date(), DateConstant.Y_M_D));
		postClickDao.save(postClick);
	}

}
