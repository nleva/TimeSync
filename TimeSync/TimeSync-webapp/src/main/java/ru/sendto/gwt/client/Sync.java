package ru.sendto.gwt.client;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.Scheduler;

import ru.sendto.dto.TimeSyncDto;
import ru.sendto.gwt.client.util.Bus;
import ru.sendto.rest.gwt.Websocket;

public class Sync  {

	public static void init() {
		Scheduler.get().scheduleFixedPeriod(Sync::sendRequest, 1000);
		sendRequest();
	}

	static int max = 10;
	private static boolean sendRequest() {
		Websocket.send(new TimeSyncDto().setClient(new Date().getTime()));
		return max-- > 0;
	}

	static Set<Integer> delta = new TreeSet<>();

	{Bus.get().listen(TimeSyncDto.class, Sync::setSync);}
	static void setSync(TimeSyncDto dto) {
		long clientTs = new Date().getTime();
		long requestDuration = clientTs - dto.getClient();
		long serverTimeShift = dto.getServer() + requestDuration / 2 - clientTs;
		delta.add(((int)serverTimeShift));
		Object[] results = delta.toArray();
		Integer c = ((Integer)results[results.length/2]);
		setServerTimeShift(c);
	}

	static native void setServerTimeShift(int shift)/*-{
		$wnd.serverTimeShift = shift;
	}-*/;

	static native int getserverTimeShift()/*-{
		return $wnd.serverTimeShift;
	}-*/;

}
