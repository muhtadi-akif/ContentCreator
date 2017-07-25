package multiplexer.contentcreator.utils;

/**
 * Created by vansikrishna on 08/06/2016.
 */
public class Constants {

    public static final int REQUEST_STORAGE_PERMS = 1;
    public static final int REQUEST_CAMERA_PERMISSION = 2;
    public static final int REQUEST_MULTIPLE_PERMISSIONS = 3;
    public static final int REQUEST_CALL_PERMS = 4;

    public static final int TYPE_MULTI_PICKER = 3;
    public static final int TYPE_MULTI_CAPTURE = 4;

    public static final String KEY_BUNDLE_LIST = "BUNDLE_LIST";
    public static final String KEY_PARAMS = "PARAMS";

    // 移轴区域的最小半径
    int BLUR_MIN_WIDTH = 40;

    // bitmap的最大宽度和高度
    double MAX_WIDTH = 800.0;

    // 进行移轴操作时的2个状态：
    // PREVIEW_IMAGE: 正在进行移轴操作，这时需要显示高模糊度的bitmap
    // FINAL_IMAGE: 移轴操作已经完成，这时需要显示低模糊度的bitmap
    int PREVIEW_IMAGE = 0;
    int FINAL_IMAGE = 1;

}
