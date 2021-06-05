package pt.up.fc.progmovel.socialapp.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {
    public static final Charset charset = StandardCharsets.UTF_16;
    public static final byte[] BLUETOOTH_TYPE_CHAT_MESSAGE = "bluetooth_type_2".getBytes(charset);
    public static final byte[] BLUETOOTH_TYPE_END_OF_MESSAGE = "bluetooth_type_3".getBytes(charset);
    public static final byte[] BLUETOOTH_TYPE_POST = "bluetooth_type_4".getBytes(charset);

    public static final String SHARED_PREFERENCES= "pt.up.fc.progmovel.socialapp.SHARED";
    public static final String SHARED_LOCAL_USER_ID= "pt.up.fc.progmovel.socialapp.shared.LOCAL_USER_ID";

    public static  final String EXTRA_CHAT_ID = "pt.up.fc.progmovel.socialapp.extra.EXTRA_CHAT_ID";
    public static  final String EXTRA_VIDEO_URI = "pt.up.fc.progmovel.socialapp.extra.EXTRA_VIDEO_URI";

    public Constants(){

    }

}
