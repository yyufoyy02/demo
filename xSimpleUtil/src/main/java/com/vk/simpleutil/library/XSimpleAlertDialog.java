package com.vk.simpleutil.library;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * AlertDialog工具类
 *
 * @author Administrator
 */
public class XSimpleAlertDialog {
    private XSimpleAlertDialog() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static int Theme;

    public static void setTheme(int theme) {
        Theme = theme;
    }

    /**
     * loading对话框
     */

    public static Dialog MyProgessDialog(Activity mContext, String msg,
                                         final Callback callback) {
        final Dialog dialog = new Dialog(mContext, R.style.SimpleMyDialog);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.myprogess_dialog, null);
        ((TextView) view.findViewById(R.id.tv_message)).setText(msg);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    if (callback != null)
                        callback.getCallback();
                    dialog.dismiss();
                }
                return false;
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;

    }

    private static AlertDialog.Builder createAlertDialog(Context mContext) {
        if (Theme != 0)
            return new AlertDialog.Builder(mContext, Theme);
        return new AlertDialog.Builder(mContext);
    }

    /**
     * 上下文对话框
     */
    public static void simpleBaseDialog(Context mContext, String title,
                                        String S1, final Callback mS1Callback) {
        createAlertDialog(mContext)
                .setTitle(title)
                .setItems(new String[]{S1},
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (mS1Callback != null)
                                    mS1Callback.getCallback();
                            }
                        })
                .create().show();
    }

    public static void simpleBaseDialog(Context mContext, String title,
                                        String S1, final Callback mS1Callback, String btntext) {
        createAlertDialog(mContext).setTitle(title).setMessage(S1)
                .setNegativeButton(btntext, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (mS1Callback != null)
                            mS1Callback.getCallback();
                        dialog.cancel();
                    }
                }).create().show();
    }

    /**
     * 上下对话框
     */

    public static void simpleBaseDialog(Context mContext, String title,
                                        String S1, final Callback mS1Callback, String S2,
                                        final Callback mS2Callback) {

        createAlertDialog(mContext)
                .setTitle(title)
                .setItems(new String[]{S1, S2},
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        if (mS1Callback != null)
                                            mS1Callback.getCallback();
                                        break;
                                    case 1:
                                        if (mS2Callback != null)
                                            mS2Callback.getCallback();
                                        break;
                                }
                            }
                        })
                .create().show();
    }

    /**
     * 上下对话框
     */
    public static void simpleBaseDialog(Context mContext, String title,
                                        String S1, final Callback mS1Callback, String S2,
                                        final Callback mS2Callback, String S3,
                                        final Callback mS3Callback) {
        createAlertDialog(mContext)
                .setTitle(title)
                .setItems(new String[]{S1, S2, S3},
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        if (mS1Callback != null)
                                            mS1Callback.getCallback();

                                        break;

                                    case 1:
                                        if (mS2Callback != null)
                                            mS2Callback.getCallback();

                                        break;
                                    case 2:
                                        if (mS3Callback != null)
                                            mS3Callback.getCallback();
                                        break;
                                }
                            }
                        })
                .create().show();
    }

    /**
     * 上下对话框
     */
    public static void simpleBaseDialog(Context mContext, String title,
                                        String S1, final Callback mS1Callback, String S2,
                                        final Callback mS2Callback, String S3,
                                        final Callback mS3Callback, String S4,
                                        final Callback mS4Callback) {
        createAlertDialog(mContext)
                .setTitle(title)
                .setItems(new String[]{S1, S2, S3, S4},
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        if (mS1Callback != null)
                                            mS1Callback.getCallback();

                                        break;

                                    case 1:
                                        if (mS2Callback != null)
                                            mS2Callback.getCallback();
                                        break;
                                    case 2:
                                        if (mS3Callback != null)
                                            mS3Callback.getCallback();
                                        break;
                                    case 3:
                                        if (mS4Callback != null)
                                            mS4Callback.getCallback();
                                        break;
                                }
                            }
                        })
                .create().show();
    }

    /**
     * 左右对话框
     */
    public static void myAlertDialog(Context mContext, String title, String msg,
                                     String leftS, final Callback mleftCallback,
                                     String rightS, final Callback mrightCallback) {
        createAlertDialog(mContext)
                .setTitle(title).setMessage(msg).setNegativeButton(leftS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (mleftCallback != null)
                    mleftCallback.getCallback();
            }
        }).setPositiveButton(rightS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mrightCallback != null)
                    mrightCallback.getCallback();
            }
        }).create().show();
    }

    /**
     * 左右对话框（全局）
     */
    public static void myAlertDialogWindow(Context mContext, String title, String msg,
                                           String leftS, final Callback mleftCallback,
                                           String rightS, final Callback mrightCallback) {
        final AlertDialog mAlertDialog = createAlertDialog(mContext)
                .setTitle(title).setMessage(msg).setNegativeButton(leftS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mleftCallback != null)
                            mleftCallback.getCallback();
                    }
                }).setPositiveButton(rightS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mrightCallback != null)
                            mrightCallback.getCallback();
                    }
                })
                .create();
        mAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mAlertDialog.setCanceledOnTouchOutside(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mAlertDialog.show();
            }
        }, 400);
    }

    /**
     * 拨打电话
     *
     * @param mContext
     * @param num
     */
    public static void callPhoneDialog(final Context mContext, String title,
                                       String num) {
        final String[] nums;
        if (num.contains(",")) {
            nums = num.split(",");
        } else if (num.contains("|")) {
            nums = num.split("\\|");
        } else {
            nums = new String[1];
            nums[0] = num;
        }
        if (nums.length == 2) {
            simpleBaseDialog(mContext, title, nums[0], new Callback() {

                @Override
                public void getCallback() {
                    if (nums[0].contains("(")) {
                        nums[0] = nums[0].replace("(", "");
                        nums[0] = nums[0].replace(")", "");
                    }
                    if (nums[0].contains("-")) {
                        nums[0] = nums[0].replace("-", "");
                    }
                    Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + nums[0]));

                    mContext.startActivity(in);

                }
            }, nums[1], new Callback() {

                @Override
                public void getCallback() {
                    if (nums[1].contains("(")) {
                        nums[1] = nums[1].replace("(", "");
                        nums[1] = nums[1].replace(")", "");
                    }
                    if (nums[1].contains("-")) {
                        nums[1] = nums[1].replace("-", "");
                    }
                    Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + nums[1]));

                    mContext.startActivity(in);

                }
            });
        } else {
            simpleBaseDialog(mContext, title, nums[0], new Callback() {

                @Override
                public void getCallback() {
                    if (nums[0].contains("(")) {
                        nums[0] = nums[0].replace("(", "");
                        nums[0] = nums[0].replace(")", "");
                    }
                    if (nums[0].contains("-")) {
                        nums[0] = nums[0].replace("-", "");
                    }
                    Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + nums[0]));

                    mContext.startActivity(in);

                }
            });
        }

    }

    public interface Callback {
        void getCallback();
    }

}
