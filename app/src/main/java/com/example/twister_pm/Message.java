package com.example.twister_pm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
//import org.apache.commons.lang.builder.ToStringBuilder;

public class Message implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("totalComments")
    @Expose
    private Integer totalComments;
    private final static long serialVersionUID = -7557869758311321708L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    @Override
   public String toString() {
       return " id: " + id + "\n content: " + content + "\n user: " + user + "\n comments: " + totalComments + "\n ";
    }

}