package ru.sendto.gwt.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.Scheduler;

import ru.sendto.dto.TimeSyncDto;
import ru.sendto.gwt.client.html.Log;
import ru.sendto.gwt.client.util.Bus;
import ru.sendto.rest.gwt.Websocket;

public class Sync {

	public static native String getTsUrl()/*-{
		return $wnd.tsUrl;
	}-*/;

	public static native void setTsUrl(String url)/*-{
		return $wnd.tsUrl = url;
	}-*/;

	public static void init() {
		Scheduler.get().scheduleFixedPeriod(Sync::sendRequest, 300);
		sendRequest();
	}

	static int max = 20;

	private static boolean sendRequest() {
		Websocket.send(new TimeSyncDto().setClient(new Date().getTime()));
		return max-- > 0;
	}

	static class TimeEntry{
		public TimeEntry(int duration, int shift) {
			super();
			this.duration = duration;
			this.shift = shift;
		}
		int duration;
		int shift;
	}
	static Set<Integer> delta = new TreeSet<>((i,j)->i>j?1:-1);
	static Set<TimeEntry> set = new TreeSet<>((i,j)->i.duration>j.duration?1:-1);

	static {Bus.get().listen(TimeSyncDto.class, Sync::setSync);}
	static void setSync(TimeSyncDto dto) {
		long clientTs = new Date().getTime();
		long requestDuration = clientTs - dto.getClient();
		long serverTimeShift =  dto.getServer() + requestDuration / 2 - clientTs; 

		final TimeEntry e = new TimeEntry((int)(clientTs-dto.getClient()), (int)serverTimeShift);
		set.add(e);
		
		if(set.size()>=3) {
			final ArrayList<TimeEntry> list = new ArrayList<>(set);
			int fistResultsCount = (int) (set.size()*0.7);
			final TreeSet<TimeEntry> filtred = new TreeSet<>((i,j)->i.shift>j.shift?1:-1);
			for(int i=0; i<fistResultsCount; i++) {
				list.get(i);
				filtred.add(list.get(i));
			}
			TimeEntry mid=(TimeEntry) filtred.toArray()[(fistResultsCount+1)/2];
			setServerTimeShift(mid.shift);
			setServerPing(mid.duration);
			
		}
	}

	public static native void setServerTimeShift(int shift)/*-{
		$wnd.serverTimeShift = shift;
	}-*/;

	public static native int getServerTimeShift()/*-{
		return $wnd.serverTimeShift;
	}-*/;

	public static native void setServerPing(int ping)/*-{
		$wnd.serverPing = ping;
	}-*/;

	public static native int getServerPing()/*-{
		return $wnd.serverPing;
	}-*/;

}
