package com.ymall.util;

import com.google.common.collect.Lists;
import com.ymall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //使所有字段都参与序列化
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消将Date字段转换成时间戳
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        //忽略空Bean错误(空Bean不报异常)
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //统一时间格式 yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDER_FORMAT));

        //忽略在json中存在而在java中找不到对应字段的的错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 序列化对象（将对象转换成json字符）
     */

    public static <T> String objToString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("parse object to json error", e);
            return null;
        }
    }

    /**
     * 序列化对象（格式化后的，比较漂亮）
     */
    public static <T> String objToPrettyString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("parse object to json error", e);
            return null;
        }
    }

    /**
     * 反序列化对象
     */
    public static <T> T stringToObject(String str, Class<T> cls) {
        if (StringUtils.isBlank(str) || cls == null) {
            return null;
        }
        try {
            return cls.equals(String.class) ? (T) str : objectMapper.readValue(str, cls);
        } catch (IOException e) {
            log.warn("parse json to object error", e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToObject(String str, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(str) || typeReference == null) {
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("parse json to object error", e);
            return null;
        }
    }

    public static <T> T stringToObject(String str, Class<?> collectionCls, Class<?> ...elementsCls) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionCls, elementsCls);

        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("parse json to object error", e);
            return null;
        }
    }




    public static void main(String[] args) {


//        User user1 = new User();
//        String json = JsonUtil.objToString(user1);
//        System.out.println(json);

//        User user1 = new User();
//        user1.setId(1);
//        user1.setUsername("yeonon");
//
//        User user2 = new User();
//        user2.setId(2);
//        user2.setUsername("weiyanyu");
//
//        List<User> userList = Lists.newArrayList();
//        userList.add(user1);
//        userList.add(user2);
//
//        String resultString = JsonUtil.objToString(userList);
//
//        List<User> userList1 = JsonUtil.stringToObject(resultString, List.class, User.class);

    }



}
