package io.mithrilcoin.mithrilplay.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.mithrilcoin.mithrilplay.R;

public class CommonDialog extends Dialog {

    private CommonDialogListener mListener = null;

    private TextView dialog_title, dialog_desc;
    private Button btn_dialog_negative, btn_dialog_positive;

    public CommonDialog(Activity activity, CommonDialogListener listener) {
        super(activity);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int)(size.x * 0.9);
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        mListener = listener;

        dialog_title = (TextView) findViewById(R.id.dialog_title);
        dialog_desc = (TextView) findViewById(R.id.dialog_desc);
        btn_dialog_negative = (Button) findViewById(R.id.btn_dialog_negative);
        btn_dialog_positive = (Button) findViewById(R.id.btn_dialog_positive);

    }

    public void showDialogOneButton(String msg, String positiveBtnName) {
        showDialogOneButton("", msg, positiveBtnName);
    }

    public void showDialogOneButton(String title, String msg, String positiveBtnName) {

        if (TextUtils.isEmpty(title)) {
            dialog_title.setVisibility(View.INVISIBLE);
        } else {
            dialog_title.setVisibility(View.VISIBLE);
            dialog_title.setText(title);
        }
        dialog_desc.setText(msg);

        btn_dialog_negative.setVisibility(View.GONE);
        btn_dialog_positive.setText(positiveBtnName);
        btn_dialog_positive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onConfirm();
                dismiss();
            }
        });

        show();
    }

    public void showDialogTwoButton(String msg, String negativeButtonName, String positiveButtonName) {

        showDialogTwoButton("", msg, negativeButtonName, positiveButtonName);
    }

    public void showDialogTwoButton(String title, String msg, String negativeButtonName, String positiveButtonName) {

        if (TextUtils.isEmpty(title)) {
            dialog_title.setVisibility(View.INVISIBLE);
        } else {
            dialog_title.setVisibility(View.VISIBLE);
            dialog_title.setText(title);
        }
        dialog_desc.setText(msg);

        btn_dialog_negative.setText(negativeButtonName);
        btn_dialog_negative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onCancel();
                dismiss();
            }
        });

        btn_dialog_positive.setText(positiveButtonName);
        btn_dialog_positive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onConfirm();
                dismiss();
            }
        });
        show();
    }

    public interface CommonDialogListener {
        public void onConfirm();

        public void onCancel();
    }

}
