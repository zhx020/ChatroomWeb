package com.su.service;

import java.util.List;

import com.su.entity.ChatRoom;

public interface ChatRoomService {

	void addChatRoom(String name);

	void deleteChatRoom(int id);

	void updateChatRoom(int id, String name);

	List<ChatRoom> getChatRooms(String begDate,String endDate);

	ChatRoom getChatRoom(int id);


}
