package com.kunzhuo.xuechelang.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangqiang on 16/5/26.
 */
public abstract class AbsDataMapper<T> implements IDataMapper<T> {
    protected Gson mGson;

    public AbsDataMapper() {
        mGson = new Gson();
    }

    public abstract T transformBean(String jsonStr) throws JsonSyntaxException;

    public abstract List<T> transformCollection(String jsonStr) throws JsonSyntaxException;


    public T transformBean(JSONObject json, String key) throws Exception {
        try {
            String code = json.getString("Code");

            if ("0".equals(code)) {
                return transformBean(json.getJSONObject(key).toString());
            }
            return null;
        } catch (Exception e) {
            throw e;
        }
    }


    public List<T> transformCollection(JSONObject json, String key) throws Exception {
        try {
            String code = json.getString("Code");

            if ("0".equals(code)) {
                return transformCollection(json.getJSONArray(key).toString());
            }
            return null;
        } catch (Exception e) {
            throw e;
        }
    }
}
