package io.uhha.coin.common.util;

import org.springframework.cglib.beans.BeanMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommenUtils {


    /**
     * @return
     */
    public static String  createC2COrderNumber(){

        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(int)((Math.random()*9+1)*100000);


    }

    /**
     * 将对象装换为map
     * @param bean
     * @return
     */

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }


//    public static void main(String[] args) {
//        String orderId = CommenUtils.createC2COrderNumber();
//        System.out.println(orderId);
//    }



}
