package edu.htl3r.schoolplanner.gui.loginTask;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

public abstract class UpdatePublishingAsyncTask<Params, Result> extends AsyncTask<Params, AsyncTaskProgress, Result> {

	private List<OnUpdatePublishingAsyncTaskListener<Result>> listener = new ArrayList<OnUpdatePublishingAsyncTaskListener<Result>>();
	
	public void addListener(OnUpdatePublishingAsyncTaskListener<Result> listener) {
		this.listener.add(listener);
	}
	
	public void removeListener(OnUpdatePublishingAsyncTaskListener<Result> listener) {
		this.listener.remove(listener);
	}
	
	@Override
	protected void onCancelled() {
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onCancelled();
		}
		
		super.onCancelled();
	}
	
	@Override
	protected void onProgressUpdate(AsyncTaskProgress... values) {
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onProgressUpdate(values[0]);
		}
		
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Result result) {
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onPostExecute(result);
		}
		
		super.onPostExecute(result);
	};
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onPreExecute();
		}
	}
	
	public void cancel() {
		cancel(true);
	}
}
