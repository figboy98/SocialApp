package pt.up.fc.progmovel.socialapp.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {
    public static final Charset charset = StandardCharsets.UTF_16;
    public static final byte[] BLUETOOTH_TYPE_GROUP_CHAT_ID_MESSAGE = "bluetooth_type_1".getBytes(charset);
    public static final byte[] BLUETOOTH_TYPE_CHAT_MESSAGE = "bluetooth_type_2".getBytes(charset);
    public static final byte[] BLUETOOTH_TYPE_END_OF_MESSAGE = "bluetooth_type_3".getBytes(charset);
    public static final byte[] BLUETOOTH_TYPE_POST = "bluetooth_type_4".getBytes(charset);

    public static final String TYPE_CHAT_MESSAGE = "ChatMessage";
    public static final String TYPE_GROUP_CHAT_ID = "GroupId";
    public static final String TYPE_POST_MESSAGE = "PostMessage";


    public Constants(){

    }

}
