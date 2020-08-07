package com.hotnews;

import com.hotnews.dao.CommentDao;
import com.hotnews.dao.LoginTicketDao;
import com.hotnews.dao.NewsDao;
import com.hotnews.dao.UserDao;
import com.hotnews.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HotNewsApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	UserDao userDao;

	@Autowired
	NewsDao newsDao;

	@Autowired
	LoginTicketDao loginTicketDao;

	@Autowired
	CommentDao commentDao;

	@Test
	public void initData() {
		Random random = new Random();
		for (int i = 0; i < 11; i++) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);

			News news = new News();
			news.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
			news.setLikeCount(i+1);
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE{%d}", i));
			news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
			newsDao.addNews(news);

			for (int j = 0; j < 3; j++) {
				Comment comment = new Comment();
				comment.setUserId(i+1);
				comment.setEntityId(news.getId());
				comment.setEntityType(EntityType.ENTITY_COMMENT);
				comment.setStatus(0);
				comment.setCreatedDate(new Date());
				comment.setContent("Comment " + String.valueOf(j));
				commentDao.addComment(comment);
			}

			user.setPassword("newpassword");
			userDao.updatePassword(user);

			LoginTicket ticket = new LoginTicket();
			ticket.setStatus(0);
			ticket.setUserId(i+1);
			ticket.setExpired(date);
			ticket.setTicket(String.format("TICKET%d",i+1));
			loginTicketDao.addTicket(ticket);

			loginTicketDao.updateStatus(ticket.getTicket(),2);
		}

		Assert.assertEquals("newpassword",userDao.selectById(1).getPassword());
		userDao.deleteById(1);
		Assert.assertNull(userDao.selectById(1));

		Assert.assertEquals(1, loginTicketDao.selectByTicket("TICKET1").getUserId());
		Assert.assertEquals(2, loginTicketDao.selectByTicket("TICKET1").getStatus());

//		Assert.assertNotNull(commentDao.selectByEntity(1,EntityType.ENTITY_NEWS).get(1));
	}



}