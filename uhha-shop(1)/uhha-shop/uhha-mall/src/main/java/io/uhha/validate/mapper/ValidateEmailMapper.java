package io.uhha.validate.mapper;

import io.uhha.validate.bean.ValidateEmailDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ValidateEmailMapper {

    int insert(ValidateEmailDO record);

    ValidateEmailDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ValidateEmailDO record);
}