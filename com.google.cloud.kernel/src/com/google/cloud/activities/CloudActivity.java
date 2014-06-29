package com.google.cloud.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.cloud.CloudKernelApplication;
import com.google.cloud.provider.DataCache;
import com.google.cloud.provider.IDataProvider;
import com.google.cloud.publisher.IChangePublisher;
import com.google.cloud.synchronization.ISyncListener;
import com.google.cloud.synchronization.ISyncWorkerPublisher;
import com.google.cloud.synchronization.task.SyncEvent;
/**
 * This class handles the initial things like register for sync updates
 * or updating the view. After a sync operation is finished
 * an update will be sent to all the registered listeners, thus, 
 * implicit to this class.
 * Basically all the classes should extend this 
 * activity.
 * This class supports adding, deleting, showing and closing of dialogs.
 * The main reason for this is that it is possible that
 * we will need to delay the starting of the application or some
 * UI interaction for some reasons. Thus if it is required
 * while the some Background operation is running, a dialog can be
 * shown. 
 * @author Andrei
 *
 */
public abstract class CloudActivity extends Activity implements ISyncListener {
	private static final String TAG = CloudActivity.class.getSimpleName();
	List<Dialog> dialogsToShow = Collections.synchronizedList(new ArrayList<Dialog>());
	protected static boolean isUpdateInProgress = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		register();
	}
	protected abstract void register();
	protected abstract void unregister();
	protected IChangePublisher getChangePublisher()
	{
		return CloudKernelApplication.getInstance().getChangePublisher();
	}
	protected abstract void registerForUpdates();
	public void updateEmptyViewVisibility(ListView listView, TextView emptyView) {

		if (listView.getCount() < 1) {

			emptyView.setVisibility(View.VISIBLE);
		} else {

			emptyView.setVisibility(View.GONE);
		}
	}
	protected DataCache getDataCache()
	{
		return CloudKernelApplication.getInstance().getDataCache();
	}
	public ISyncWorkerPublisher getSyncWorkerPublisher()
	{
		return CloudKernelApplication.getInstance().getSyncManager().getSyncWorkerPublisher();
	}
	public void addDialog(DialogType type, String title) {
		switch (type) {
		case PROGRESS:
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(title);
			dialog.setCancelable(true);
			updateDialogs(dialog);
			break;

		default:
			Log.d(TAG, "For now We don't support other dialogs");
			break;
		}
	}
	protected IDataProvider getDataProvider()
	{
		return CloudKernelApplication.getInstance().getDataProvider();
	}
	private void updateDialogs(Dialog dialog) {
		Iterator<Dialog> it = dialogsToShow.listIterator();
		while (it.hasNext()) {
			Dialog refDialog = it.next();
			if (refDialog != null && !refDialog.isShowing()) {
				dialogsToShow.remove(refDialog);
			}
		}
		dialogsToShow.add(dialog);
	}

	public void showDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (Dialog dialog : dialogsToShow) {
					if (dialog != null && !dialog.isShowing()) {
						dialog.show();
					}
				}
			}
		});
	}

	@Override
	protected void onPause() {
		closeAllDialogs();
		unregister();
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		closeAllDialogs();
		unregister();
		super.onDestroy();
	}

	private void closeAllDialogs() {
		for (Dialog dialog : dialogsToShow) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}
		dialogsToShow.clear();
	}
	public void addDialog(Dialog dialog)
	{
		if(!dialogsToShow.contains(dialog))
		{
			dialogsToShow.add(dialog);
		}
	}
	protected void inflateOptionsMenu(Menu menu, int... menuIds)
	{
		MenuInflater menuInflater = getMenuInflater();
		for(int k = 0; k < menuIds.length;k++)
		{
			menuInflater.inflate(menuIds[k], menu);
		}
	}
	@Override
	public void receivedSyncNotification(SyncEvent update) {
		Log.d(TAG, "handles messages after the sync manager finished the job");
		if (update.equals(SyncEvent.UPDATE)) {
			Log.d(TAG, "Starts an update.");
			isUpdateInProgress = true;
			Log.d(TAG, "The update concept is not really used right now.");
		}
		else if(update.equals(SyncEvent.FINISHED))
		{
			isUpdateInProgress = false;
			onUpdateFinished();
			Log.d(TAG, "FInished updating.");
			//closeAllDialogs();
		}
	}
	protected abstract void initLayout();
	protected abstract void onUpdateStarted();
	protected abstract void onUpdateFinished();
}
