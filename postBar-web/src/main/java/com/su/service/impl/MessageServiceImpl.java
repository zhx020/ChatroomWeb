package com.su.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.su.base.constant.DateConstant;
import com.su.base.util.DateUtil;
import com.su.dao.MessageDao;
import com.su.entity.MessageBoard;
import com.su.entity.Message;
import com.su.service.MessageBoardService;
import com.su.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService{
	@Autowired
	private MessageDao messageDao; 
	@Autowired
	private MessageBoardService chatBoardService;
	
	@Override
	public void addMessage(String content,int boardId,String name) {
		Message message = new Message();
		message.setContent(content);
		message.setName(name);
		message.setBoardId(boardId);
		message.setPublishDate(DateUtil.dateConvertStr(new Date(), DateConstant.Y_M_D));
		MessageBoard chatBoard = chatBoardService.getMessageBoard(boardId);
		chatBoard.addMessageCount();
		messageDao.save(message);
	}

	@Override
	public void deleteMessage(int id) {
		Message message = messageDao.get(Message.class, id);
		MessageBoard chatBoard = chatBoardService.getMessageBoard(message.getBoardId());
		chatBoard.subMessageCount();
		message.setIsDelete(1);
	}

	@Override
	public void updateMessage(int id, String name,String content) {
		Message message = messageDao.get(Message.class, id);
		message.setName(name);
		message.setContent(content);
	}

	@Override
	public List<Message> getMessages(Integer boardId, String begDate, String endDate) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("from " + Message.class.getSimpleName() + " where isDelete = 0");
		if(boardId != null) {
			sql.append("and boardId = ?");
			params.add(boardId);
		}
		if(StringUtils.isNoneBlank(begDate)) {
			sql.append("and boardId = ?");
			params.add(boardId);
		}
		if(StringUtils.isNoneBlank(endDate)) {
			sql.append("and boardId = ?");
			params.add(endDate);
		}
		List<Message> queryList = messageDao.queryList(sql.toString(), params);
		if(CollectionUtils.isNotEmpty(queryList)) {
			for(Message message : queryList) {
				message.setBoardName(chatBoardService.getMessageBoard(message.getBoardId()).getName());
			}
		}
		return queryList;
	}

	@Override
	public Message getMessage(int id) {
		Message message = messageDao.get(Message.class, id);
		MessageBoard chatBoard = chatBoardService.getMessageBoard(message.getBoardId());
		message.setBoardName(chatBoard.getName());
		return message;
	}
}
