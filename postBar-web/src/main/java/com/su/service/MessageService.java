package com.su.service;

import java.util.List;
import com.su.entity.Message;

public interface MessageService {

	void addMessage(String content,int boardId,String name);

	void deleteMessage(int id);

	void updateMessage(int id, String name,String content);

	List<Message> getMessages(Integer boardId,String begDate,String endDate);

	Message getMessage(int id);

}
