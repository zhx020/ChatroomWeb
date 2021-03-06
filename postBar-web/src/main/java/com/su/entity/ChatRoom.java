package com.su.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.su.base.constant.DBConst;

@Entity
@Table(name=DBConst.CHATROOM)
public class ChatRoom {
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private Integer id;
	@Column(name="name",length=255)
	private String name;//比赛名称
	@Column(name="is_Delete",length=1)
	private int isDelete;//是否删除
	@Column(name="post_Count",length=10)
	private int postCount;//帖子数量
	@Transient
	private String isUpdate = "not";//是否已经修改
	@Transient
	private int clickTimes;
	
	public int getClickTimes() {
		return clickTimes;
	}
	public void setClickTimes(int clickTimes) {
		this.clickTimes = clickTimes;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
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
	public int getPostCount() {
		return postCount;
	}
	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public void addPostCount() {
		this.postCount ++;
	}
	public void subPostCount() {
		this.postCount --;
	}
	
}
