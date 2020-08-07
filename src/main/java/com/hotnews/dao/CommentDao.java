package com.hotnews.dao;

import com.hotnews.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/7 22:59
 * Remark:
 */
@Repository
@Mapper
public interface CommentDao {
	String TABLE_NAME = "comment";
	String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,") values " +
			"(#{userId},#{content}, #{createdDate}, #{entityId}, #{entityType}, #{status})"})
	int addComment(Comment comment);

	@Select({"select ", SELECT_FIELDS, "from", TABLE_NAME,
			"where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
	List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

	@Select({"select count(id) from",TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
	int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

	//真删除 暂时保留
	@Delete({"select ", SELECT_FIELDS, "from", TABLE_NAME,
			"where id=#{id}"})
	void deleteById(@Param("id") int Id);

	//假删真改
	@Update({"update ", TABLE_NAME, "set status = #{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
	void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

}
