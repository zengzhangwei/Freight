package com.zl.freight.utils;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public class AddressUtils implements OnGetSuggestionResultListener {

    private final SuggestionSearch mSuggestionSearch;

    public AddressUtils() {
        //热词搜索
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    public void search(String data) {
        String city = data.substring(0, (data.indexOf("市") + 1));
        String search = data.substring((data.indexOf("市") + 1), data.length());
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(search)
                .city(city));
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        double latitude = 0, longitude = 0;
        if (suggestionResult != null || suggestionResult.getAllSuggestions() != null) {
            List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
            if (allSuggestions.size() > 0){
                SuggestionResult.SuggestionInfo suggestionInfo = allSuggestions.get(0);
                latitude = suggestionInfo.pt.latitude;
                longitude = suggestionInfo.pt.longitude;
            }
        }
        if (onAddressSearchListener != null) {
            onAddressSearchListener.onSearch(latitude, longitude);
        }
    }

    private OnAddressSearchListener onAddressSearchListener;

    public void setOnAddressSearchListener(OnAddressSearchListener onAddressSearchListener) {
        this.onAddressSearchListener = onAddressSearchListener;
    }

    public interface OnAddressSearchListener {
        void onSearch(double x, double y);
    }

}
