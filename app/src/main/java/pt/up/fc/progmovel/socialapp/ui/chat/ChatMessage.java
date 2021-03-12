package pt.up.fc.progmovel.socialapp.ui.chat;

import java.util.Date;

public class ChatMessage {

    private String mTextMessage, mFrom, mTo;
    private Date mDate;

    public ChatMessage(){

    }
    public ChatMessage(String str, Date date, String from, String to){
        mTextMessage = str;
        mDate = date;
        mFrom = from;
        mTo = to;
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
