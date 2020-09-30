package com.su.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.su.base.constant.DBConst;

@Entity
@Table(name=DBConst.POST)
public class Post {
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;
	@Column(name="name",length=255)
	private String name;//帖子名称
	@Column(name="content",length=255)
	private String content;//帖子内容
	@Column(name="room_Id",length=10)
	private int roomId;//聊天室ID
	@Column(name="click_Times",length=10)
	private int clickTimes;//点击次数
	@Column(name="publish_Date",length=20)
	private String publishDate;//发布日期
	@Column(name="is_Delete",length=20)
	private int isDelete;//是否删除
	@Transient
	private String roomName;//所属聊天室
	
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getClickTimes() {
		return clickTimes;
	}
	public void setClickTimes(int clickTimes) {
		this.clickTimes = clickTimes;
	}
	public void addClickTimes() {
		this.clickTimes ++;
	}
	
}
