package kr.co.kjworld.viewsearch.data.response.data;

import java.util.ArrayList;
import java.util.List;

public class KakaoData {
    public List<Document> documents;
    public Meta meta;

    public KakaoData() {
        documents = new ArrayList<>();
    }
}
