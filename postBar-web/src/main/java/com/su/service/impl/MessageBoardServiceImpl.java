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
import com.su.dao.MessageBoardDao;
import com.su.entity.MessageBoard;
import com.su.entity.PostClick;
import com.su.service.MessageBoardService;

@Service
public class MessageBoardServiceImpl implements MessageBoardService{
	public Set<Integer> updateds = new HashSet<>();
	@Autowired
	private MessageBoardDao messageBoardDao;
	
	@Override
	public void addMessageBoard(String name) {
		MessageBoard chatRoom = new MessageBoard();
		chatRoom.setName(name);
		messageBoardDao.save(chatRoom);
	}

	@Override
	public void deleteMessageBoard(int id) {
		MessageBoard chatRoom = messageBoardDao.get(MessageBoard.class, id);
		chatRoom.setIsDelete(1);
	}

	@Override
	public void updateMessageBoard(int id, String name) {
		MessageBoard chatRoom = messageBoardDao.get(MessageBoard.class, id);
		chatRoom.setName(name);
		updateds.add(id);
	}

	@Override
	public List<MessageBoard> getMessageBoards(String begDate,String endDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("from " + MessageBoard.class.getSimpleName() + " where isDelete = 0");
		List<MessageBoard> queryList = messageBoardDao.queryList(sql.toString(), null);
		
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
		
		for(MessageBoard chatRoom : queryList) {
			if(updateds.size() > 0) {
				if(updateds.contains(chatRoom.getId())) {
					chatRoom.setIsUpdate("yes");
				}
				updateds.clear();
			}
			params.set(0, chatRoom.getId());
		}
		Collections.sort(queryList, new Comparator<MessageBoard>() {

			@Override
			public int compare(MessageBoard o1, MessageBoard o2) {
				return o2.getClickTimes() - o1.getClickTimes();
			}
		});
		return queryList;
	}

	@Override
	public MessageBoard getMessageBoard(int id) {
		return messageBoardDao.get(MessageBoard.class, id);
	}

}
