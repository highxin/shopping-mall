package com.mall.common.utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;


public class HashUtils {


    public static String generateSkuHash(List<Long> ids){

        //排序
        String source = ids.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining("_"));


        try {

            MessageDigest md =
                    MessageDigest.getInstance("MD5");


            byte[] bytes =
                    md.digest(
                        source.getBytes(StandardCharsets.UTF_8)
                    );


            StringBuilder sb = new StringBuilder();


            for(byte b : bytes){

                sb.append(
                    String.format("%02x",b)
                );

            }


            return sb.toString();


        }catch (Exception e){

            throw new RuntimeException(e);
        }

    }

}