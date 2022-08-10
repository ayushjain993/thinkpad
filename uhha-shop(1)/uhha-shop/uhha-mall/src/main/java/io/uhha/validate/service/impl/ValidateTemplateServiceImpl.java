package io.uhha.validate.service.impl;


import java.util.List;
import java.util.Map;

import io.uhha.common.utils.DateUtils;
import io.uhha.system.service.IMallRedisInitService;
import io.uhha.validate.service.IValidateTemplateService;
import io.uhha.validate.bean.ValidateTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.validate.mapper.ValidateTemplateMapper;

/**
 * 校验Service业务层处理
 *
 * @author peter
 * @date 2022-01-27
 */
@Service
public class ValidateTemplateServiceImpl implements IValidateTemplateService
{
    @Autowired
    private ValidateTemplateMapper validateTemplateMapper;

    @Autowired
    private IMallRedisInitService mallRedisInitService;

    /**
     * 查询校验
     *
     * @param id 校验主键
     * @return 校验
     */
    @Override
    public ValidateTemplate selectValidateTemplateById(Integer id)
    {
        return validateTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询校验列表
     *
     * @param map 校验
     * @return 校验
     */
    @Override
    public List<ValidateTemplate> selectValidateTemplateList(Map<String, Object> map)
    {
        return validateTemplateMapper.selectListByPage(map);
    }

    /**
     * 新增校验
     *
     * @param validateTemplate 校验
     * @return 结果
     */
    @Override
    public int insertValidateTemplate(ValidateTemplate validateTemplate)
    {
        validateTemplate.setGmtCreate(DateUtils.getNowDate());
        validateTemplateMapper.insert(validateTemplate);
        mallRedisInitService.initValidateTemplate();
        return 1;
    }

    /**
     * 修改校验
     *
     * @param validateTemplate 校验
     * @return 结果
     */
    @Override
    public int updateValidateTemplate(ValidateTemplate validateTemplate)
    {
        validateTemplate.setGmtModified(DateUtils.getNowDate());
        validateTemplateMapper.updateByPrimaryKey(validateTemplate);
        mallRedisInitService.initValidateTemplate();
        return 1;
    }

    /**
     * 批量删除校验
     *
     * @param ids 需要删除的校验主键
     * @return 结果
     */
    @Override
    public int deleteValidateTemplateByIds(Integer[] ids)
    {
        for (int i = 0; i < ids.length; i++) {
            validateTemplateMapper.deleteByPrimaryKey(ids[i]);
        }
        mallRedisInitService.initValidateTemplate();
        return ids.length;
    }

    /**
     * 删除校验信息
     *
     * @param id 校验主键
     * @return 结果
     */
    @Override
    public int deleteValidateTemplateById(Integer id)
    {
        mallRedisInitService.initValidateTemplate();
        return validateTemplateMapper.deleteByPrimaryKey(id);
    }
}