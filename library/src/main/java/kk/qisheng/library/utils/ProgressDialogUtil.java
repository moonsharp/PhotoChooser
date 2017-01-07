package kk.qisheng.library.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by KkQiSheng on 2017/1/7.
 */

public class ProgressDialogUtil {
    private static ProgressDialog mDialog;

    public static void showDialog(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public static void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
