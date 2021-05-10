package pt.up.fc.progmovel.socialapp.ui.chat;

import android.media.Image;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity
public class  ChatMessage {
    @PrimaryKey (autoGenerate = false)
    private UUID chatMessageID;
    private String mTextMessage;
    private String mFrom;
    private String mTo;
    private String mType;
    private Date mDate;

    public ChatMessage() {

    }

    public ChatMessage(String message, Date date, String from, String to, String type) {
        chatMessageID = UUID.randomUUID();
        mTextMessage = message;
        mDate = date;
        mFrom = from;
        mTo = to;
        mType= type;
    }
    public  UUID getChatMessageID(){ return  chatMessageID; }

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
