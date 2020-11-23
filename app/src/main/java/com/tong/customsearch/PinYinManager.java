package com.tong.customsearch;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PinYinManager {
    private static final String TAG = "PinYinManager";

    /**
     * setToneType 设置音标的显示方式：
     *
     * HanyuPinyinToneType.WITH_TONE_MARK：在拼音字母上显示音标，如“zhòng”
     * HanyuPinyinToneType.WITH_TONE_NUMBER：在拼音字符串后面通过数字显示，如“zhong4”
     * HanyuPinyinToneType.WITHOUT_TONE：不显示音标
     * setCaseType 设置拼音大小写：
     *
     * HanyuPinyinCaseType.LOWERCASE：返回的拼音为小写字母
     * HanyuPinyinCaseType.UPPERCASE：返回的拼音为大写字母
     * setVCharType 设置拼音字母“ü”的显示方式
     * 汉语拼音中的“ü”不能简单的通过英文来表示，所以需要单独定义“ü”的显示格式
     *
     * HanyuPinyinVCharType.WITH_U_UNICODE：默认的显示方式，输出“ü”
     * HanyuPinyinVCharType.WITH_V：输出“v”
     * HanyuPinyinVCharType.WITH_U_AND_COLON：输出“u:”
     */


    /**
     *  获取中文拼音（不包含多音字）
     * @param list 中文数据集
     * @param userNameList 处理后的数据集
     */
    public static void getPinYins(List<String> list,List<UserName> userNameList){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不显示音标
        format.setVCharType(HanyuPinyinVCharType.WITH_V);//“ü”输出V
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//拼音输出小写
        for (String name : list) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int j=0;j<name.length();j++){
                char c = name.charAt(j);
                String[] cStrHY = new String[0];
                try {
                    cStrHY = PinyinHelper.toHanyuPinyinStringArray(c,format);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                stringBuffer.append(cStrHY[0]);
            }
            UserName userName = new UserName();
            userName.setPinyin(stringBuffer.toString());
            userName.setName(name);
            userNameList.add(userName);
        }
    }

    /**
     * 获取中文拼音 （包含多音字）
     * @param strings 中文数据集
     * @param pinYins 多拼音集合
     * @param index 下标
     */
    public static void getPinYinKey(List<String> strings, List<String[]> pinYins, int index,PinYinCallBack pinYinCallBack) {

        List<String> stringList = new ArrayList<>();
        for (String s : strings) {
            for (String s2 : pinYins.get(index)) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(s);
                stringBuffer.append(s2);
                stringList.add(stringBuffer.toString());
            }
        }
        if (pinYins.size() > index + 1) {
            getPinYinKey(stringList, pinYins, index + 1,pinYinCallBack);
        } else {
            removal(stringList);
            pinYinCallBack.callBack(stringList);
//            for (String pinyin : stringList) {
//                Log.e(TAG, "pinyin: " + pinyin);
//            }
        }
    }

    /**
     * 去重
     * @param stringList 数据源
     */
    public static void removal(List<String> stringList){
        HashSet<String> hashSet = new HashSet<>(stringList);
        stringList.clear();
        stringList.addAll(hashSet);
    }

    interface PinYinCallBack{
        void callBack(List<String> pinyins);
    }

}
