package com.hotnews.dao;

import com.hotnews.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: XianDaLi
 * Date: 2020/8/8 0:25
 * Remark:
 */
@Repository
@Mapper
public interface MessageDao {
	String TABLE_NAME = " message ";
	String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date ";
	String SELECT_FIELDS = " id, " + INSERT_FIELDS;

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
			") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate})"})
	int addMessage(Message message);

	//查询与每个人相关的消息列表(倒叙)，只与每个人消息中最新的显示一条，并且显示消息数量。
	/*@Select({"select ", INSERT_FIELDS, " ,count(id) as id from " +
		"( select",INSERT_FIELDS, "from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt " +
	"group by conversation_id order by id desc limit #{offset},#{limit}"})*/
	/*
	SELECT *, count(id) as cnt from
        (SELECT * FROM message where from_id=12 or to_id=12 order by id desc) tt
    group by conversation_id order by id desc;*/
	@Select({"select ", INSERT_FIELDS, " ,count(id) as id from " +
			"( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt " +
			"group by conversation_id order by id desc limit #{offset},#{limit}"})
	List<Message> getConversationList(@Param("userId") int userId,
	                                  @Param("offset") int offset,
	                                  @Param("limit") int limit);

	@Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
	int getConversationUnReadCount(@Param("userId") int userId,
	                               @Param("conversationId") String conversationId);

	@Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId}"})
	int getConversationTotalCount(@Param("userId") int userId,
	                              @Param("conversationId") String conversationId);

	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}"})
	List<Message> getConversationDetail(@Param("conversationId") String conversationId,
	                                    @Param("offset") int offset,
	                                    @Param("limit") int limit);
}
