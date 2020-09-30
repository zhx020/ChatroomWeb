package com.su.dao.impl;

import org.springframework.stereotype.Repository;

import com.su.base.dao.impl.BaseDaoImpl;
import com.su.dao.MessageDao;
import com.su.entity.Message;

@Repository
public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao{

}
