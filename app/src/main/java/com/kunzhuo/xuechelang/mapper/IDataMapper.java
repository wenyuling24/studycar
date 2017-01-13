package com.kunzhuo.xuechelang.mapper;

import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangqiang on 16/5/26.
 */
public interface IDataMapper<T> {
    T transformBean(String jsonStr) throws JsonSyntaxException;

    List<T> transformCollection(String jsonStr) throws JsonSyntaxException;

    T transformBean(JSONObject json, String key) throws Exception;

    List<T> transformCollection(JSONObject json, String key) throws Exception;
}
