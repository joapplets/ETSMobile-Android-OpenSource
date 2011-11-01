package com.applets.utils.xml;


public interface IAsyncTaskListener {

    public void onPostExecute();

    public void onProgressUpdate(Integer[] values);
}
