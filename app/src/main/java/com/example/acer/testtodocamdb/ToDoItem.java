package com.example.acer.testtodocamdb;

/**
 * Created by Acer on 12/5/2016.
 */

public class ToDoItem {

    //MEMBER ATTRIBUTES
    private int _id;
    private String description;
    private String imageFile = null;
    private int is_done;

    public ToDoItem(){

    }

    public ToDoItem(String desc, String imageFile, int done){
        setDescription(desc);
        setImageFile(imageFile);
        setIs_done(done);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getIs_done() {
        return is_done;
    }

    public void setIs_done(int is_done) {
        this.is_done = is_done;
    }
}
