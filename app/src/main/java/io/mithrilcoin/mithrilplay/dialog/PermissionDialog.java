package io.mithrilcoin.mithrilplay.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import io.mithrilcoin.mithrilplay.R;

public class PermissionDialog extends Dialog {

    private PermissionConfirmListener mListener = null;

    public PermissionDialog(Activity activity, PermissionConfirmListener listener) {
        super(activity);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_permission);

        mListener = listener;

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mListener != null) {
//                        mListener.onConfirm();
                }
            }
        });
    }

    public void showDialogOneButton() {

        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onConfirm();
                dismiss();
            }
        });

        show();
    }

    public interface PermissionConfirmListener {
        public void onConfirm();
    }

}
