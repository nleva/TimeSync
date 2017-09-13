package ru.sendto.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import ru.sendto.rest.gwt.Websocket;

public class SyncModuleEP implements EntryPoint {

	@Override
	public void onModuleLoad() {
		String url = Sync.getTsUrl();
		if(url==null)
			url="tsync";
		Websocket.init(GWT.getHostPageBaseURL() + url);
		Sync.init();
	}
	
	


}
