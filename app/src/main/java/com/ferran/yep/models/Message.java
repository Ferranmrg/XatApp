package com.ferran.yep.models;

import android.graphics.Bitmap;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ferra on 22/01/2016.
 */
public class Message implements Serializable {
    String from;
    String to;
    String message;
    byte[] image;
    ParseFile video;
    Date fecha;

    public Message(String from, String to, String message, byte[] image, ParseFile video) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = image;
        this.video = video;
    }

    public Message(String from, String to, String message, byte[] image) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = image;
        this.video = null;
    }

    public Message(String from, String to, String message, Date fecha) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = null;
        this.video = null;
        this.fecha = fecha;
    }

    public Message(String from, String to, byte[] image) {
        this.from = from;
        this.to = to;
        this.message = null;
        this.image = image;
        this.video = null;

    }

    public Message() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ParseFile getVideo() {
        return video;
    }

    public void setVideo(ParseFile video) {
        this.video = video;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
