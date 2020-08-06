package com.hotnews.service;

import com.hotnews.dao.NewsDao;
import com.hotnews.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/6 0:05
 * Remark:
 */
@Service
public class NewsService {
	@Autowired
	private NewsDao newsDao;

	public List<News> getLatestNews(int userId, int offset, int limit){
		return newsDao.selectByUserIdAndOffset(userId, offset, limit);
	}
}
