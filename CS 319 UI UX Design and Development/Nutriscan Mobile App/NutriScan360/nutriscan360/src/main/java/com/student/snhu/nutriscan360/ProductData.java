package com.student.snhu.nutriscan360;

import java.util.Map;

public class ProductData {
    public String product_name;
    public String brands;
    public String image_url;
    public Nutriments nutriments;

    public static class Nutriments {
        public double energy_kcal_100g;
        public double fat_100g;
        public double saturated_fat_100g;
        public double sugars_100g;
        public double salt_100g;
        public double proteins_100g;
    }
}
