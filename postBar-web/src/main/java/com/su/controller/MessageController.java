package com.su.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.entity.Message;
import com.su.service.MessageService;

@Controller
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	private String preffix = "message/";
	
	@RequestMapping(value="message",method= {RequestMethod.POST})
	@ResponseBody
	public void addMessage(HttpServletRequest request,String content,int boardId,String name) {
		messageService.addMessage(content,boardId,name);
	}
	@RequestMapping(value="message/{id}",method= {RequestMethod.DELETE})
	@ResponseBody
	public void deleteChatRoom(@PathVariable("id") int id) {
		messageService.deleteMessage(id);
	}
	
	@RequestMapping(value="message/{id}",method= {RequestMethod.PUT})
	@ResponseBody
	public void updateChatRoom(HttpServletRequest request,@PathVariable("id") int id,
			String content,String name) {
		messageService.updateMessage(id,name,content);
	}
	
	@RequestMapping(value="message",method= {RequestMethod.GET})
	public String toChatRoom(HttpServletRequest request,
			Integer boardId,String begDate,String endDate) {
		List<Message> messages = messageService.getMessages(boardId, begDate, endDate);
		request.setAttribute("messages", messages);
		return preffix + "message";
	}
	
	@RequestMapping(value="message/toUpdate/{id}",method= {RequestMethod.GET})
	public String toUpdate(HttpServletRequest request,@PathVariable("id") int id) {
		Message message = messageService.getMessage(id);
		request.setAttribute("message", message);
		return preffix + "updateMessage";
	}
	
	@RequestMapping(value="message/toDeail/{id}",method= {RequestMethod.GET})
	public String toDeail(HttpServletRequest request,@PathVariable("id") int id) {
		Message message = messageService.getMessage(id);
		request.setAttribute("message", message);
		return preffix + "messageDetail";
	}
	
	
}
