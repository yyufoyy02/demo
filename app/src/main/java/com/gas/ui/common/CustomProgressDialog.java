package com.property.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.epiboly.R;


public class CustomProgressDialog extends Dialog {
    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
    private CustomProgressDialog customProgressDialog = null;
    public static CustomProgressDialog createDialog(Context context) {
        CustomProgressDialog
        customProgressDialog = new CustomProgressDialog(context,
                R.style.ShareProgressDialog);
        customProgressDialog.setContentView(R.layout.share_progress_layout);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.customProgressDialog = customProgressDialog;
        return customProgressDialog;
    }

    public static CustomProgressDialog createDialog(Context context, int styleRes, int layoutRes) {
        CustomProgressDialog
                customProgressDialog = new CustomProgressDialog(context,
                styleRes);
        customProgressDialog.setContentView(layoutRes);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.customProgressDialog = customProgressDialog;
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (customProgressDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) customProgressDialog
                .findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public CustomProgressDialog setTitile(String strTitle) {
        return customProgressDialog;
    }

    public CustomProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.share_loading_msg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return customProgressDialog;
    }

    @Override
    public void dismiss() {
        try {

            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                super.dismiss();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
