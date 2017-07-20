package com.hxw.frame.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.StringRes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences工具类
 * 其实没有必要,这主要是存放用户信息的
 * Created by hxw on 2017/2/25.
 */
public class SPUtils {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;
    /**
     * 这类是为适配API9一下时使用的,通常我设置最低19,所以不会用
     * private static SharedPreferencesCompat.EditorCompat editorCompat =
     * SharedPreferencesCompat.EditorCompat.getInstance();
     */
//    /**
//     * 静态内部类,一种单例模式的写法
//     */
//    private static class SPHolder {
//        private static final SPUtils instance = new SPUtils();
//    }
    private static SPUtils instance;

    public static SPUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SPUtils(context);
        }
        return instance;
    }

    private SPUtils(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(getSPName(context), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    private String getSPName(Context context) {
        return context.getPackageName() + "user";
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public SPUtils putString(@StringRes int key, String value) {
        return putString(mContext.getString(key), value);
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putString(String key, String value) {
        editor.putString(key, value).apply();
        return this;
    }

    /**
     * SP中读取String
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getString(@StringRes int key, String defValue) {
        return getString(mContext.getString(key), defValue);
    }

    /**
     * SP中读取String
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defValue}
     */
    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putInt(@StringRes int key, int value) {
        return putInt(mContext.getString(key), value);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putInt(String key, int value) {
        editor.putInt(key, value).apply();
        return this;
    }

    /**
     * SP中读取int
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getInt(@StringRes int key, int defValue) {
        return getInt(mContext.getString(key), defValue);
    }

    /**
     * SP中读取int
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putFloat(@StringRes int key, float value) {
        return putFloat(mContext.getString(key), value);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putFloat(String key, float value) {
        editor.putFloat(key, value).apply();
        return this;
    }

    /**
     * SP中读取float
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloat(@StringRes int key, float defValue) {
        return getFloat(mContext.getString(key), defValue);
    }

    /**
     * SP中读取float
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putLong(@StringRes int key, long value) {
        return putLong(mContext.getString(key), value);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putLong(String key, long value) {
        editor.putLong(key, value).apply();
        return this;
    }

    /**
     * SP中读取long
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLong(@StringRes int key, long defValue) {
        return getLong(mContext.getString(key), defValue);
    }

    /**
     * SP中读取long
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putBoolean(@StringRes int key, boolean value) {
        return putBoolean(mContext.getString(key), value);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
        return this;
    }

    /**
     * SP中读取boolean
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBoolean(@StringRes int key, boolean defValue) {
        return getBoolean(mContext.getString(key), defValue);
    }

    /**
     * SP中读取boolean
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * SP中写入Set<Sting>集合
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putStringSet(@StringRes int key, Set<String> value) {
        return putStringSet(mContext.getString(key), value);
    }

    /**
     * SP中写入Set<Sting>集合
     *
     * @param key   键
     * @param value 值
     */
    public SPUtils putStringSet(String key, Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            editor.putStringSet(key, value);
        }
        return this;
    }


    /**
     * SP中读取Set<Sting>集合
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public Set<String> getStringSet(@StringRes int key, Set<String> defValue) {
        return getStringSet(mContext.getString(key), defValue);
    }

    /**
     * SP中读取Set<Sting>集合
     *
     * @param key      键
     * @param defValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public Set<String> getStringSet(String key, Set<String> defValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return sharedPreferences.getStringSet(key, defValue);
        } else {
            return new HashSet<>(0);
        }
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public SPUtils remove(@StringRes int key) {
        return remove(mContext.getString(key));
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public SPUtils remove(String key) {
        editor.remove(key).apply();
        return this;
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean contains(@StringRes int key) {
        return contains(mContext.getString(key));
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * SP中清除所有数据
     */
    public SPUtils clear() {
        editor.clear().apply();
        return this;
    }
}