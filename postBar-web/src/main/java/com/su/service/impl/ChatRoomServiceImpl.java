package com.su.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.su.dao.ChatRoomDao;
import com.su.dao.PostClickDao;
import com.su.entity.ChatRoom;
import com.su.entity.PostClick;
import com.su.service.ChatRoomService;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{
	public Set<Integer> updateds = new HashSet<>();
	@Autowired
	private ChatRoomDao chatRoomDao;
	@Autowired
	private PostClickDao postClickDao;
	
	@Override
	public void addChatRoom(String name) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setName(name);
		chatRoomDao.save(chatRoom);
	}

	@Override
	public void deleteChatRoom(int id) {
		ChatRoom chatRoom = chatRoomDao.get(ChatRoom.class, id);
		chatRoom.setIsDelete(1);
	}

	@Override
	public void updateChatRoom(int id, String name) {
		ChatRoom chatRoom = chatRoomDao.get(ChatRoom.class, id);
		chatRoom.setName(name);
		updateds.add(id);
	}

	@Override
	public List<ChatRoom> getChatRooms(String begDate,String endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("from " + ChatRoom.class.getSimpleName() + " where isDelete = 0");
		List<ChatRoom> queryList = chatRoomDao.queryList(sql.toString(), null);
		
		StringBuilder countSql = new StringBuilder("from " + PostClick.class.getSimpleName() + " where roomId = ?");
		List<Object> params = new ArrayList<Object>();
		params.add("?");
		if(StringUtils.isNoneBlank(begDate)) {
			countSql.append("and date >= ?");
			params.add(begDate);
		}
		if(StringUtils.isNoneBlank(endDate)) {
			countSql.append("and date <= ?");
			params.add(endDate);
		}
		
		for(ChatRoom chatRoom : queryList) {
			if(updateds.size() > 0) {
				if(updateds.contains(chatRoom.getId())) {
					chatRoom.setIsUpdate("yes");
				}
			}
			params.set(0, chatRoom.getId());
			chatRoom.setClickTimes(postClickDao.getResultCount(countSql.toString(), params));
		}
		updateds.clear();
		Collections.sort(queryList, new Comparator<ChatRoom>() {

			@Override
			public int compare(ChatRoom o1, ChatRoom o2) {
				return o2.getClickTimes() - o1.getClickTimes();
			}
		});
		return queryList;
	}

	@Override
	public ChatRoom getChatRoom(int id) {
		return chatRoomDao.get(ChatRoom.class, id);
	}

}
