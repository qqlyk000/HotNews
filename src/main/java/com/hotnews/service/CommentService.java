package com.hotnews.service;

import com.hotnews.dao.CommentDao;
import com.hotnews.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/7 23:20
 * Remark:
 */
@Service
public class CommentService {
	private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

	@Autowired
	CommentDao commentDao;

	public List<Comment> getCommentsByEntity(int entityId, int entityType){
		return commentDao.selectByEntity(entityId,entityType);
	}

	public int addComment(Comment comment){
		return commentDao.addComment(comment);
	}

	public int getCommentCount(int entityId, int entityType){
		return commentDao.getCommentCount(entityId,entityType);
	}

	public void deleteComment(int entityId, int entityType){
		commentDao.updateStatus(entityId,entityType,1);
	}
}
