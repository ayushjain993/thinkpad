package io.uhha.validate.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.validate.bean.ValidateTemplate;

/**
 * 校验Service接口
 *
 * @author peter
 * @date 2022-01-27
 */
public interface IValidateTemplateService
{
    /**
     * 查询校验
     *
     * @param id 校验主键
     * @return 校验
     */
    public ValidateTemplate selectValidateTemplateById(Integer id);

    /**
     * 查询校验列表
     *
     * @param map 校验
     * @return 校验集合
     */
    public List<ValidateTemplate> selectValidateTemplateList(Map<String, Object> map);

    /**
     * 新增校验
     *
     * @param validateTemplate 校验
     * @return 结果
     */
    public int insertValidateTemplate(ValidateTemplate validateTemplate);

    /**
     * 修改校验
     *
     * @param validateTemplate 校验
     * @return 结果
     */
    public int updateValidateTemplate(ValidateTemplate validateTemplate);

    /**
     * 批量删除校验
     *
     * @param ids 需要删除的校验主键集合
     * @return 结果
     */
    public int deleteValidateTemplateByIds(Integer[] ids);

    /**
     * 删除校验信息
     *
     * @param id 校验主键
     * @return 结果
     */
    public int deleteValidateTemplateById(Integer id);
}