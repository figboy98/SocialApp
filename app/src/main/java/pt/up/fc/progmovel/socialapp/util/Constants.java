package pt.up.fc.progmovel.socialapp.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public  class Constants {
    public Charset charset = StandardCharsets.UTF_16;
    public byte[] BLUETOOTH_TYPE_GROUP_CHAT_ID_MESSAGE = new String("bluetooth_type_1").getBytes(charset);
    public byte[] BLUETOOTH_TYPE_CHAT_MESSAGE = new String("bluetooth_type_2").getBytes(charset);
    public byte[] BLUETOOTH_TYPE_END_OF_MESSAGE = new String("bluetooth_type_3").getBytes(charset);
    public byte[] BLUETOOTH_TYPE_POST = new String("bluetooth_type_4").getBytes(charset);

    public String TYPE_CHAT_MESSAGE = "ChatMessage";
    public String TYPE_GROUP_CHAT_ID = "GroupId";
    public String TYPE_POST_MESSAGE = "PostMessage";


    public Constants(){

    }

}
