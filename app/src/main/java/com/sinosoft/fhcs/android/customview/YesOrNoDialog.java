package com.sinosoft.fhcs.android.customview;

import com.sinosoft.fhcs.android.R;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class YesOrNoDialog extends Dialog {

    public YesOrNoDialog(Context context) {
        super(context);
    }

    public YesOrNoDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String msg;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.msg = message;
            return this;
        }

        /**
         * Set the Dialog message from resource 
         *
         * @param msg
         * @return
         */
        public Builder setMessage(int message) {
            this.msg = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource 
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String 
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }



        /**
         * Set the positive button resource and it's listener 
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public YesOrNoDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme  
            final YesOrNoDialog dialog = new YesOrNoDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.popup_deletemsg_all, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            //set title
            ((TextView) layout.findViewById(R.id.title_text)).setText(title);
            // set the dialog message
            ((TextView) layout.findViewById(R.id.pop_remove_msg_tv_name)).setText(msg);
            // set the confirm button  
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.pop_remove_msg_btn_cancle))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.pop_remove_msg_btn_cancle))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.pop_remove_msg_btn_cancle).setVisibility(
                        View.GONE);
            }
            // set the cancel button  
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.pop_remove_msg_btn_sure))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.pop_remove_msg_btn_sure))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.pop_remove_msg_btn_sure).setVisibility(
                        View.GONE);
            }

            return dialog;
        }
    }
}
