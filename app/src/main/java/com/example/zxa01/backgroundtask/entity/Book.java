package com.example.zxa01.backgroundtask.entity;

public class Book {
    private String title;
    private String author;
    private String printType;

    public Book(String title,String author,String printType){
        this.title = title;
        this.author = author;
        this.printType = printType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }
}
