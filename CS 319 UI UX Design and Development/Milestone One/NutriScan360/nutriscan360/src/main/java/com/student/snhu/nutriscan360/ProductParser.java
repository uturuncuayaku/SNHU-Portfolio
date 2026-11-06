package com.student.snhu.nutriscan360;

import com.google.gson.*;

public class ProductParser {

    public static ProductData parse(String json) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject product = root.getAsJsonObject("product");

            Gson gson = new Gson();
            return gson.fromJson(product, ProductData.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
