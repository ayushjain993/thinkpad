package io.uhha.validate.mapper;


import io.uhha.validate.bean.ValidateSmsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ValidateSmsMapper {

    int insert(ValidateSmsDO record);

    ValidateSmsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ValidateSmsDO record);
}