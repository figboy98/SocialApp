package pt.up.fc.progmovel.socialapp.ui.chat;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity
public class  ChatMessage {

    @PrimaryKey (autoGenerate = false)
    @NonNull
    private String chatMessageID;
    private String mTextMessage;
    private String mFrom;
    private String mTo;
    private String mType;
    private String mDate;

    public ChatMessage() {

    }

    public ChatMessage(String message, Date date, String from, String to, String type) {
        chatMessageID = UUID.randomUUID().toString();
        mTextMessage = message;
        mDate = date.toString();
        mFrom = from;
        mTo = to;
        mType= type;
    }

    public  String getChatMessageID(){ return  chatMessageID; }

    public String getTextMessage() {
        return mTextMessage;
    }

    public String getDate() { return mDate; }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }

    public String getType(){
        return mType;
    }

    public void setChatMessageID(String id){
        chatMessageID = id;
    }

    public void setTextMessage(String textMessage) {
        mTextMessage = textMessage;
    }

    public void setDate(String date) { mDate = date; }

    public void setFrom(String from) {
        mFrom = from;
    }

    public void setType(String type){
        mType = type;
    }

    public void setTo(String to) {
        mTo = to;
    }

}
