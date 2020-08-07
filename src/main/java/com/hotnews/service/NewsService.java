package com.hotnews.service;

import com.hotnews.dao.NewsDao;
import com.hotnews.model.News;
import com.hotnews.util.HotNewsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

	//添加新闻
	public int addNews(News news){
		newsDao.addNews(news);
		return news.getId();
	}

	//根据id查找新闻
	public News getById(int newsId){
		return newsDao.getById(newsId);
	}

	//更新评论数
	public int updateCommentCount(int id,int count){
		return newsDao.updateCommentCount(id, count);
	}

	//上传图片文件
	public String saveImage(MultipartFile file) throws IOException{
		int dotPos = file.getOriginalFilename().lastIndexOf(".");
		if(dotPos < 0){
			return null;
		}
		String fileExt = file.getOriginalFilename().substring(dotPos+1).toLowerCase();
		if(HotNewsUtil.isFileAllowed(fileExt)){
			return null;
		}

		String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + fileExt;
		Files.copy(file.getInputStream(), new File(HotNewsUtil.IMAGE_DIR + fileName).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		// xxxx.jpg
		return HotNewsUtil.HOTNEWS_DOMAIN + "image?name=" + fileName;
	}
}
