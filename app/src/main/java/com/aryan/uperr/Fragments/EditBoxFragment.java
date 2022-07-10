package com.aryan.uperr.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.aryan.uperr.R;
import com.aryan.uperr.models.AlarmModel;
import com.aryan.uperr.utils.DatabaseHandler;
import com.aryan.uperr.utils.DialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class EditBoxFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ActionDialog";
    TextView title_view;
    EditText editText;
    Button done;
    DatabaseHandler db;

    public static EditBoxFragment getInstance() {
        return new EditBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_box_layout, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        if (getActivity() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                Rect displayFrame = new Rect();
                decorView.getWindowVisibleDisplayFrame(displayFrame);
                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int heightDifference = height - displayFrame.bottom;
                if (heightDifference != 0) {
                    if (v.getPaddingBottom() != heightDifference) {
                        v.setPadding(0, 0, 0, heightDifference);
                    }
                } else {
                    if (v.getPaddingBottom() != 0) {
                        v.setPadding(0, 0, 0, 0);
                    }
                }
            });
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        editText = requireView().findViewById(R.id.editText);
        done = requireView().findViewById(R.id.saveButton);



        //handle adding to box
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if(bundle != null) {
            isUpdate = true;
            String name = bundle.getString("name");
            editText.setText(name);
            if(name.length() > 0) {
                setBottomColor(done, true);
            }
        }

        editText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}
            public void afterTextChanged(Editable editable) {}


            public void onTextChanged(CharSequence string, int start, int before, int count) {
                setBottomColor(done, !string.toString().equals(""));
            }
        });

        boolean finalIsUpdate = isUpdate;
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                Toast.makeText(getContext(), "done clicked", Toast.LENGTH_LONG).show();
                if (finalIsUpdate) {
                    db.updateName(bundle.getInt("id"), text);
                } else {
                    AlarmModel model = new AlarmModel(text, 0);
                    db.insertAlarm(model);
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editText.setText(null);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose();
        }
    }

    private void setBottomColor(Button button, boolean isNonEmpty) {
        if (isNonEmpty) {
            button.setEnabled(true);
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSecondary));
        } else {
            button.setEnabled(false);
            button.setTextColor(getResources().getColor(R.color.gray));
        }
    }
}
