package com.tong.customsearch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<String> list;
    private SearchPinYinAdapter searchPinYinAdapter;
    private List<UserName> userNameList;

    private EditText etSearchName;
    private EditText etSearchPinYin;

    public static final String[] str = new String[]{
            "单雄信","王重阳","徐明"
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
        recyclerView.setVisibility(View.GONE);

        list = new ArrayList<>();
        list.addAll(Arrays.asList(str));

        userNameList = new ArrayList<>();

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不显示音标
        format.setVCharType(HanyuPinyinVCharType.WITH_V);//“ü”输出V
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);//拼音输出小写
        for (final String s : list) {
            List<String[]> pinYinManagerBeans = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                String[] cStrHY = new String[0];
                try {
                    cStrHY = PinyinHelper.toHanyuPinyinStringArray(c, format);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                pinYinManagerBeans.add(cStrHY);
            }

            List<String> strings = new ArrayList<>();
            strings.addAll(Arrays.asList(pinYinManagerBeans.get(0)));
            if (pinYinManagerBeans.size() > 1) {
                PinYinManager.getPinYinKey(strings, pinYinManagerBeans, 1, new PinYinManager.PinYinCallBack() {
                    @Override
                    public void callBack(List<String> pinyins) {
                        for (String pinyin : pinyins) {
                            UserName userName = new UserName();
                            userName.setPinyin(pinyin);
                            userName.setName(s);
                            userNameList.add(userName);
                        }
                    }
                });
            }else {
                for (String string : strings) {
                    UserName userName = new UserName();
                    userName.setPinyin(string);
                    userName.setName(s);
                    userNameList.add(userName);
                }
            }
        }
//        for (UserName userName : userNameList) {
//            Log.e(TAG, "onCreate: "+userName.toString() );
//        }

        //搜名字
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


        //搜拼音
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
                if (recyclerView.getVisibility()==View.GONE){
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
