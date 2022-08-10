package io.uhha.common.enums;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 交易API Constant
 *
 * @author dp
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEnum {


    /**
     * 审核类型：
     *
     * @author mallplus
     */
    public enum AuditType implements BaseEnum<Integer> {

        /**
         * 待审核
         */
        INIT(0, "待审核"),
        /**
         * 审核通过
         */
        SUCCESS(1, "审核通过"),
        /**
         * 审核失败
         */
        FAIL(2, "审核失败"),
        ;

        private int code;
        private String value;

        AuditType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer code() {
            return code;
        }

        @Override
        public String desc() {
            return value;
        }
    }

    /**
     * 审核类型：
     *
     * @author mallplus
     */
    public enum YesNoType implements BaseEnum<Integer> {

        /**
         * 启用
         */
        YES(1, "yes"),

        /**
         * 禁用
         */
        NO(0, "no"),
        ;

        private int code;
        private String value;

        YesNoType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer code() {
            return code;
        }

        @Override
        public String desc() {
            return value;
        }
    }

    /**
     * 商品上架状态： 0 下架  1上架 2违规下架 默认0
     *
     * @author peter
     */
    public enum ShelfType implements BaseEnum<Integer> {

        /**
         * 下架（默认）
         */
        NO_AVAILABLE(0, "下架"),

        /**
         * 上架
         */
        AVAILABLE(1, "上架"),

        /**
         * 违规下架
         */
        FORBIDDEN(2,"违规下架")
        ;

        private int code;
        private String value;

        ShelfType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer code() {
            return code;
        }

        @Override
        public String desc() {
            return value;
        }
    }
}
