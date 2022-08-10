package io.uhha.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.im.domain.Message;
import org.springframework.transaction.annotation.Transactional;


@Transactional(rollbackFor = Exception.class)
public interface MessageMapper extends BaseMapper<Message> {

}
