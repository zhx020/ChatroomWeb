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
import com.su.entity.MessageBoard;
import com.su.service.MessageBoardService;

@Controller
public class MessageBoardController {
	private String preffix = "message/";
	@Autowired
	private MessageBoardService messageBoardService;
	
	@RequestMapping(value="messageBoard",method= {RequestMethod.POST})
	@ResponseBody
	public String addMessageBoard(HttpServletRequest request,String name) {
		messageBoardService.addMessageBoard(name);
		return "redirect:/messageBoard";
	}
	@RequestMapping(value="messageBoard/{id}",method= {RequestMethod.DELETE})
	@ResponseBody
	public String deleteMessageBoard(@PathVariable("id") int id) {
		messageBoardService.deleteMessageBoard(id);
		return null;
	}
	
	@RequestMapping(value="messageBoard/{id}",method= {RequestMethod.PUT})
	@ResponseBody
	public String updateMessageBoard(HttpServletRequest request,@PathVariable("id") int id,
			String name) {
		messageBoardService.updateMessageBoard(id,name);
		return preffix + "messageBoard";
	}
	
	@RequestMapping(value="messageBoard",method= {RequestMethod.GET})
	public String toMessageBoard(HttpServletRequest request,String begDate,String endDate) {
		List<MessageBoard> messageBoards = messageBoardService.getMessageBoards(begDate,endDate);
		request.setAttribute("messageBoards", messageBoards);
		return preffix + "messageBoard";
	}
	
	@RequestMapping(value="messageBoard/toUpdate/{id}",method= {RequestMethod.GET})
	public String toUpdate(HttpServletRequest request,@PathVariable("id") int id) {
		HttpSession session = request.getSession();
		MessageBoard messageBoard = messageBoardService.getMessageBoard(id);
		session.setAttribute("messageBoard", messageBoard);
		return preffix + "updateMessageBoard";
	}
	
	
}
