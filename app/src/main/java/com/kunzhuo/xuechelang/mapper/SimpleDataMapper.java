package com.kunzhuo.xuechelang.mapper;

import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 满足gson直接解析
 * Created by zhangqiang on 16/5/21.
 */
public class SimpleDataMapper<T> extends AbsDataMapper<T> {
    private Type mType;

    public SimpleDataMapper(Type type) {
        super();
        this.mType = type;
    }

    @Override
    public T transformBean(String jsonStr) throws JsonSyntaxException {
        try {
            T bean = mGson.fromJson(jsonStr, mType);
            return bean;
        } catch (JsonSyntaxException e) {
            throw e;
        }
    }

    @Override
    public List<T> transformCollection(String jsonStr) throws JsonSyntaxException {
        try {
            List<T> list = mGson.fromJson(jsonStr, mType);
            return list;
        } catch (JsonSyntaxException e) {
            throw e;
        }
    }
}
