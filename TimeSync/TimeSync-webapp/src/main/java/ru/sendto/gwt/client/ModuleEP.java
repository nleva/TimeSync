package ru.sendto.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import ru.sendto.rest.gwt.Websocket;

public class ModuleEP implements EntryPoint {

	@Override
	public void onModuleLoad() {
		Websocket.init(GWT.getHostPageBaseURL() + "ws");
		Sync.init();
	}

}
