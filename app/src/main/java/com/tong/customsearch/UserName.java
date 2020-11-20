package com.tong.customsearch;

public class UserName {

    private String pinyin;

    private String name;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserName{" +
                "pinyin='" + pinyin + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
