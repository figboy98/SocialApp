package pt.up.fc.progmovel.socialapp.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public  class Constants {
    public Charset charset = StandardCharsets.UTF_16;
    public final byte[] BLUETOOTH_TYPE_GROUP_CHAT_ID_MESSAGE = new String("bluetooth_type_1").getBytes(charset);
    public final byte[] BLUETOOTH_TYPE_CHAT_MESSAGE = new String("bluetooth_type_2").getBytes(charset);
    public final byte[] BLUETOOTH_TYPE_END_OF_MESSAGE = new String("bluetooth_type_3").getBytes(charset);
    public final byte[] BLUETOOTH_TYPE_POST = new String("bluetooth_type_4").getBytes(charset);

    public final String TYPE_CHAT_MESSAGE = "ChatMessage";
    public final String TYPE_GROUP_CHAT_ID = "GroupId";
    public final String TYPE_POST_MESSAGE = "PostMessage";

    public final String EXTRA_USER_ID="pt.up.fc.progmovel.socialapp.extra.USER_ID";
    public final String SHARED_LOCAL_USER_ID = "pt.up.fc.progmovel.socialapp.shared.LOCAL_USER_ID";
    public final String EXTRA_CHAT_ID = "pt.up.fc.progmovel.socialapp.extra.CHAT_ID";
    public final String SHARED_PREFERENCES = "pt.up.fc.progmovel.socialapp.shared.PREFERENCES";





    public Constants(){

    }

}
