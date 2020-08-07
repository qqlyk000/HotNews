package com.hotnews.dao;

import com.hotnews.model.News;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/5 23:35
 * Remark:
 */
@Repository            //加上这个在service里依赖注入@Autowired的时候不会有红下划线
@Mapper
public interface NewsDao {
	String TABLE_NAME = "news";
	String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
			") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
	int addNews(News news);

	@Select({"select ", SELECT_FIELDS, " from ",TABLE_NAME, "where id = #{id}"})
	News getById(int userId);

	@Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
	int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

	List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
	                                   @Param("limit") int limit);

}