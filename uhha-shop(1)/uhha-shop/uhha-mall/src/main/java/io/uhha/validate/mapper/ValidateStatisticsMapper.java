package io.uhha.validate.mapper;

import io.uhha.validate.bean.ValidateStatisticsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ValidateStatisticsMapper {

    int insert(ValidateStatisticsDO record);

    ValidateStatisticsDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ValidateStatisticsDO record);
}