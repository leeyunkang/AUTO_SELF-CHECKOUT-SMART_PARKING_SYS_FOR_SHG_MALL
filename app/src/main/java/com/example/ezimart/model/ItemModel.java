package com.example.ezimart.model;


public class ItemModel {
    public ItemModel(String key, String name, String image, String price,String weight, String barcode, String category ) {
        this.key = key;
        this.name = name;
        this.image = image;
        this.price = price;
        this.weight = weight;
        this.barcode = barcode;
        this.category = category;
    }

    private String key;
    private String name;
    private String image;
    private String price;
    private String weight;
    private String barcode;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public ItemModel() {

    }
    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {



        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
