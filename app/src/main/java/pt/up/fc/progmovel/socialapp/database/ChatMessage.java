package pt.up.fc.progmovel.socialapp.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
public class  ChatMessage implements Serializable, Comparable<ChatMessage> {

    @PrimaryKey ()
    @NonNull
    private String chatMessageID;
    private String mTextMessage;
    private String mFrom;
    private String mTo;
    private String mType;
    private Long mDate;
    @Ignore
    private byte[] dataBytes;
    public ChatMessage(){}
    public ChatMessage(String message, Date date, String from, String to, String type) {
        chatMessageID = UUID.randomUUID().toString();
        mTextMessage = message;
        mDate = date.getTime();
        mFrom = from;
        mTo = to;
        mType= type;
    }

    @NotNull
    public  String getChatMessageID(){ return  chatMessageID; }

    public String getTextMessage() {
        return mTextMessage;
    }

    public Long getDate() { return mDate; }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }

    public String getType(){
        return mType;
    }

    public byte[] getDataBytes(){return dataBytes;}

    public void setChatMessageID(@NotNull String id){
        chatMessageID = id;
    }

    public void setTextMessage(String textMessage) {
        mTextMessage = textMessage;
    }

    public void setDate(Long date) { mDate = date; }

    public void setFrom(String from) {
        mFrom = from;
    }

    public void setType(String type){
        mType = type;
    }

    public void setTo(String to) {
        mTo = to;
    }

    public byte[] getByte(){
        byte[] bytes = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try{
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  bytes;
    }
    public void setByte(byte[] bytes){
        this.dataBytes = bytes;
    }
    @Override
    public int compareTo(ChatMessage o) {
        long time = this.mDate -o.mDate;
        if(time <0){
            return -1;
        }
        else if(time >0){
            return 1;
        }
        return 0;
    }
}
