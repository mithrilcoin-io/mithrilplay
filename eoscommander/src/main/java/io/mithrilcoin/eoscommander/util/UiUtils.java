/*
 * Copyright (c) 2017 Mithril coin.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.mithrilcoin.eoscommander.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import io.mithrilcoin.eoscommander.R;
import io.mithrilcoin.eoscommander.ui.suggestion.AccountAdapter;
import io.mithrilcoin.eoscommander.ui.suggestion.WhitSpaceTokenizer;

/**
 * Created by swapnibble on 2017-08-24.
 */

public class UiUtils {

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static void setTextAndMoveCursorToEnd(EditText editText, CharSequence data ){
        if ( null != data ) {
            editText.setText(data);
            editText.setSelection(data.length());
        }
    }

    public static void setupAccountHistory( AutoCompleteTextView... autoTextViewArray ) {
        for ( AutoCompleteTextView actv : autoTextViewArray ) {
            AccountAdapter adapter = new AccountAdapter(actv.getContext(), R.layout.account_suggestion, R.id.eos_account);
            if (actv instanceof MultiAutoCompleteTextView) {
                ((MultiAutoCompleteTextView) actv).setTokenizer(new WhitSpaceTokenizer());
            }
            actv.setThreshold(1);
            actv.setAdapter(adapter);
        }
    }
}
