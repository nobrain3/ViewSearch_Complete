package kr.co.kjworld.viewsearch.data.network;

import android.text.Html;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONArray;

import java.lang.reflect.Type;

import kr.co.kjworld.viewsearch.data.response.data.Document;
import kr.co.kjworld.viewsearch.data.response.data.KakaoData;
import kr.co.kjworld.viewsearch.data.response.data.Meta;

public class KakaoDataDeserializer implements JsonDeserializer<KakaoData> {
    @Override
    public KakaoData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!KakaoData.class.getTypeName().equals(typeOfT.getTypeName()))
            return null;

        KakaoData data = new KakaoData();

        JsonObject imageJsonObject =  json.getAsJsonObject();
        JsonArray documentArray = imageJsonObject.getAsJsonArray("documents");

        for (int i = 0; i < documentArray.size(); i++)
        {
            Document document = new Document();
            JsonObject ownerJsonObject = documentArray.get(i).getAsJsonObject();
            if (ownerJsonObject.get("blogname") != null) {
                document.name = ownerJsonObject.get("blogname").getAsString();
                document.isCafe = false;
            } else if (ownerJsonObject.get("cafename") != null) {
                document.name = ownerJsonObject.get("cafename").getAsString();
                document.isCafe = true;
            }
            document.thumbnail = ownerJsonObject.get("thumbnail").getAsString();
            document.datetime = ownerJsonObject.get("datetime").getAsString();
            document.title = Html.fromHtml(ownerJsonObject.get("title").getAsString()).toString();
            document.url = ownerJsonObject.get("url").getAsString();
            document.contents = Html.fromHtml(ownerJsonObject.get("contents").getAsString()).toString();

            data.documents.add(document);
        }

        JsonObject metaObject = imageJsonObject.getAsJsonObject("meta");
        //metaObject.get("is_end").getAsBoolean();
        data.meta = new Meta();
        data.meta.setEnd(metaObject.get("is_end").getAsBoolean());
        data.meta.setPageableCount(metaObject.get("pageable_count").getAsInt());
        data.meta.setTotalCount(metaObject.get("total_count").getAsInt());


        return data;
    }
}
