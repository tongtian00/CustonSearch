package com.tong.customsearch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<String> list;
    private SearchPinYinAdapter searchPinYinAdapter;
    private List<UserName> userNameList;

    private EditText etSearchName;
    private EditText etSearchPinYin;

    public static final String[] str = new String[]{
            "陈天丽","黄正","徐明"
            ,"李自成","林子祥","周星星"
            ,"周润发","林星辰","林青霞"
            ,"李赛凤","刘德华","胡歌"
            ,"霍建华","林心如","赵薇"
            ,"赵四","赵本山","郭德纲"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearchName = findViewById(R.id.etSearchName);
        etSearchPinYin = findViewById(R.id.etSearchPinYin);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        list.addAll(Arrays.asList(str));

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
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不显示音标
        format.setVCharType(HanyuPinyinVCharType.WITH_V);//“ü”输出V
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//拼音输出小写
        userNameList = new ArrayList<>();
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

//        adapter = new SearchAdapter(this,list);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemListener(new SearchAdapter.OnItemListener() {
//            @Override
//            public void onItem(String name) {
//                etSearchName.setText(name);
//            }
//        });
//
//        etSearchName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.getFilter().filter(etSearchName.getText().toString().trim());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        searchPinYinAdapter = new SearchPinYinAdapter(this,userNameList);
        recyclerView.setAdapter(searchPinYinAdapter);
        searchPinYinAdapter.setOnItemListener(new SearchPinYinAdapter.OnItemListener() {
            @Override
            public void onItem(String name) {
                etSearchPinYin.setText(name);
            }
        });

        etSearchPinYin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchPinYinAdapter.getFilter().filter(etSearchPinYin.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
