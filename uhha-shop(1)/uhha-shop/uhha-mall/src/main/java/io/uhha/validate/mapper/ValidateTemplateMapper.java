package io.uhha.validate.mapper;

import io.uhha.validate.bean.ValidateTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ValidateTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ValidateTemplate record);

    ValidateTemplate selectByPrimaryKey(Integer id);

    List<ValidateTemplate> selectAll();

    int updateByPrimaryKey(ValidateTemplate record);

    List<ValidateTemplate> selectListByPage(Map<String, Object> map);

    Integer countListByPage(Map<String, Object> map);

    ValidateTemplate selectTemplateByParams(Map<String, Object> map);
}