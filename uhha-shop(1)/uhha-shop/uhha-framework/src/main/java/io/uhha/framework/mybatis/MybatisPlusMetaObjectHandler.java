package io.uhha.framework.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MybatisPlusMetaObjectHandler  implements MetaObjectHandler {
    /**
     *  插入的时候自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // fieldName 是实体中的属性名称
        // 检查是否有这个属性
        boolean createTime = metaObject.hasSetter("createTime");
        if (createTime) {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    /**
     * 修改的时候自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        /**
         *  先检查时候已经传入值了，若传入值了，则以传入值为准，就不再自动填充
         */
        Object updateTime = getFieldValByName("updateTime", metaObject);
        // 若没有传入值才自动填充
        if (updateTime == null) {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}
