package io.uhha.util;

import java.text.DecimalFormat;

/**
 * @author jf
 * @date 2019/09/19
 */
public class LocationUtils {
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离
     */
    public static Double getDistance(String lat1, String lng1, String lat2,
                                     String lng2) {
        double radLat1 = rad(Double.parseDouble(lat1));
        double radLat2 = rad(Double.parseDouble(lat2));
        double a = radLat1 - radLat2;
        double b = rad(Double.parseDouble(lng1)) - rad(Double.parseDouble(lng2));
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }

    /**
     * 从gps字符串中获取经度（123.45, 23.22），返回123.45
     * @param gps gps字符串
     * @return 经度
     */
    public static String getLongitude(String gps){
        if(null == gps || !gps.contains(",")){
            return null;
        }
        String[] split = gps.split(",");
        if(split.length != 2){
            return null;
        }
        return split[0];
    }

    /**
     * 从gps字符串中获取经度（123.45, 23.22），返回23.22
     * @param gps gps字符串
     * @return 维度
     */
    public static String getLatitude(String gps){
        if(null == gps || !gps.contains(",")){
            return null;
        }
        String[] split = gps.split(",");
        if(split.length != 2){
            return null;
        }
        return split[1];
    }

    public static void main(String[] args) {
        double distance = getDistance("34.2464320000", "108.9534750000",
                "34.2464320000", "108.9534750000");
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("距离" + df.format(distance / 1000) + "公里");
    }
}
