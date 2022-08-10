package io.uhha.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.im.domain.Session;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;


@Transactional(rollbackFor = Exception.class)
public interface SessionMapper extends BaseMapper<Session> {

	@Delete("delete from im_session where uid = #{uid} and nid = #{nid}")
	void delete(@Param("uid") String uid,@Param("nid") String nid);

	@Delete("delete from im_session where host = #{host} ")
	void deleteAll(@Param("host")String host);

	@Update("update im_session set state = #{state} where uid = #{uid} and nid = #{nid}")
	void updateState(@Param("uid")String uid, @Param("nid")String nid, @Param("state")int state);

}
