package org.example.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class DurakData extends AbstractData{
    private JSONArray duraklar;

    public DurakData() throws IOException {
        super();
        setDuraklar(super.get_data());
    }

    public JSONArray getDuraklar() {
        return this.duraklar;
    }

    private void setDuraklar(String data) {
        JSONObject json_data = new JSONObject(data);
        this.duraklar = (JSONArray) json_data.get("duraklar");
    }

}
