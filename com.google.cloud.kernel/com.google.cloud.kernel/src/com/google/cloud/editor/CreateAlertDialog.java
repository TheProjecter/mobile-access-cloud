package com.google.cloud.editor;

import java.util.UUID;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.R;

public class CreateAlertDialog extends AlertDialog {

	private static final String TAG = CreateAlertDialog.class.getSimpleName();
	private String value;
	private IEditorDialogCallback callback;
	private EditText editor;
	private UUID editorUid;
	protected CreateAlertDialog(Context context, 
								String message, 
								String value, 
								UUID randomUid,
								IEditorDialogCallback callback) {
		super(context);
		this.value = value;
		this.callback = callback;
		this.editorUid = randomUid;
		initializeView(message, value);
	}
	private void initializeView(String message, String value)
	{
		String ok = CloudKernelApplication.getStringResource(R.string.button_ok, "OK");
		String cancel = CloudKernelApplication.getStringResource(R.string.button_cancel, "Cancel");
		setMessage(message);
		setView(getContentView());
		setButton(AlertDialog.BUTTON_POSITIVE, ok, getSaveListener(EditorAction.OK));
		setButton(AlertDialog.BUTTON_NEUTRAL, cancel,(OnClickListener)null);
	}
	private OnClickListener getSaveListener(final EditorAction action)
	{
		return new DialogInterface.OnClickListener() 
		{		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(TAG, "showing the save listener for the dialog");
				callback.onEditorFinished(editorUid, value, editor.getText().toString(), action);
			}
		};
	}
	private View getContentView()
	{
		View dialogView  = (View)LayoutInflater.from(getContext()).inflate(R.layout.editor_layout, null, false);
		editor = (EditText)dialogView.findViewById(R.id.et_editor_value);
		editor.setText(this.value);
		return dialogView;
	}
}
