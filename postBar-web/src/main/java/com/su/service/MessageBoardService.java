package com.su.service;

import java.util.List;

import com.su.entity.MessageBoard;

public interface MessageBoardService {

	void addMessageBoard(String name);

	void deleteMessageBoard(int id);

	void updateMessageBoard(int id, String name);

	List<MessageBoard> getMessageBoards(String begDate,String endDate);

	MessageBoard getMessageBoard(int id);


}
