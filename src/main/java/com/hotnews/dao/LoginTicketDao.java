package com.hotnews.dao;

import com.hotnews.model.LoginTicket;
import com.hotnews.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Author: XianDaLi
 * Date: 2020/8/5 15:30
 * Remark:
 */
@Repository
@Mapper
public interface LoginTicketDao {

	String TABLE_NAME = "login_ticket";
	String INSERT_FIELDS = "user_id, expired, status, ticket ";
	String SELECT_FIELDS = "id, " + INSERT_FIELDS;

	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
			") values (#{userId},#{expired}, #{status}, #{ticket})"})
	int addTicket(LoginTicket ticket);

	//真删除 暂时保留
	@Delete({"delete from ", TABLE_NAME, " where ticket=#{ticket}"})
	void deleteByTicket(@Param("ticket") String ticket);

	//假删除 更新状态码
	@Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
	void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

	@Select({"select ", SELECT_FIELDS," from",TABLE_NAME," where ticket=#{ticket}"})
	LoginTicket selectByTicket(String ticket);
}
