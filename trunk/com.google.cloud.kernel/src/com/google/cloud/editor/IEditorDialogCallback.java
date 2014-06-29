package com.google.cloud.editor;

import java.util.UUID;

/**
 * A callback interface for a caller of IEditor.
 */
public interface IEditorDialogCallback {

	/**
	 */
	public void onEditorFinished(UUID editorID, String originalValue, String newValue, EditorAction action);

}