package app.myproject.yujincoffee_app.Modle.Util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToStore {
    private String jsonString;
    private SQLiteDatabase db;

    public JsonToStore(SQLiteDatabase db) {
        this.db = db;
    }


    public void writeToDatabase(String jsonString){
        this.jsonString=jsonString;
        try {
            Cursor cursor=db.rawQuery("select * from store;",null);
            if(cursor.getCount()==0) {
                JSONArray rawData = new JSONArray(jsonString);
                for (int i = 0; i < rawData.length(); i++) {
                    JSONObject jsonObject = rawData.getJSONObject(i);
                    db.execSQL("insert into store values(?,?,?,?);",
                            new Object[]{
                                    jsonObject.getString("store_name"),
                                    jsonObject.getString("location"),
                                    jsonObject.getString("telephone"),
                                    jsonObject.getString("openinghour")
                            });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
