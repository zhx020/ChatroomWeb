package com.su.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.su.entity.ChatRoom;
import com.su.service.ChatRoomService;

@Controller
public class ChatRoomController {
	@Autowired
	private ChatRoomService chatRoomService;
	
	@RequestMapping(value="chatRoom",method= {RequestMethod.POST})
	public String addChatRoom(HttpServletRequest request,String name) {
		chatRoomService.addChatRoom(name);
		return "redirect:/chatRoom";
	}
	@RequestMapping(value="chatRoom/{id}",method= {RequestMethod.DELETE})
	@ResponseBody
	public String deleteChatRoom(@PathVariable("id") int id) {
		chatRoomService.deleteChatRoom(id);
		return null;
	}
	
	@RequestMapping(value="chatRoom/{id}",method= {RequestMethod.PUT})
	@ResponseBody
	public String updateChatRoom(HttpServletRequest request,@PathVariable("id") int id,
			String name) {
		chatRoomService.updateChatRoom(id,name);
		return "chatRoom";
	}
	
	@RequestMapping(value="chatRoom",method= {RequestMethod.GET})
	public String toChatRoom(HttpServletRequest request,String begDate,String endDate) {
		List<ChatRoom> chatRooms = chatRoomService.getChatRooms(begDate,endDate);
		request.setAttribute("chatRooms", chatRooms);
		return "chatRoom";
	}
	
	@RequestMapping(value="chatRoom/toUpdate/{id}",method= {RequestMethod.GET})
	public String toUpdate(HttpServletRequest request,@PathVariable("id") int id) {
		HttpSession session = request.getSession();
		ChatRoom chatRoom = chatRoomService.getChatRoom(id);
		session.setAttribute("chatRoom", chatRoom);
		return "updateChatRoom";
	}
	
	
}
