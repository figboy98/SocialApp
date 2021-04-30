package pt.up.fc.progmovel.socialapp.ui.chat;

import android.media.Image;

import java.util.Date;

public class ChatMessage {

    private String mTextMessage, mFrom, mTo, mType;
    private Date mDate;

    public ChatMessage() {

    }

    public ChatMessage(String message, Date date, String from, String to, String type) {
        mTextMessage = message;
        mDate = date;
        mFrom = from;
        mTo = to;
        mType= type;
    }

    public String getTextMessage() {
        return mTextMessage;
    }

    public Date getDate() {
        return mDate;
    }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }

    public String getType(){
        return mType;
    }

    public void setTextMessage(String textMessage) {
        mTextMessage = textMessage;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setFrom(String from) {
        mFrom = from;
    }

    public void setTo(String to) {
        mTo = to;
    }
}
