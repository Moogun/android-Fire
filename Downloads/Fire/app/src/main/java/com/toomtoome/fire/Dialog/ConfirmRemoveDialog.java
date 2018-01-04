package com.toomtoome.fire.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.toomtoome.fire.R;


/**
 * Created by moogunjung on 11/25/17.
 */

public class ConfirmRemoveDialog extends DialogFragment {

    public interface OnConfirmRemoveListener {
        public void onComfirmRemove(String password);
    }

    OnConfirmRemoveListener mOnConfirmRemoveListener;

    private static final String TAG = "ConfirmRemoveDialog";
    private TextView mRemove;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);

        mRemove = (TextView) view.findViewById(R.id.confirm_password);

        TextView confirmDialog = (TextView) view.findViewById(R.id.dialogConfirm);

        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: captured password and confirming.");

                String password = mRemove.getText().toString();

                if (!password.equals("")) {
                    mOnConfirmRemoveListener.onComfirmRemove(password);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.empty_password_field), Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the dialog");
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnConfirmRemoveListener = (OnConfirmRemoveListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: Class Cast Exception" + e.getMessage());
        }
    }
}
