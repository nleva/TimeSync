/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sendto.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

import ru.sendto.dto.TimeSyncDto;
import ru.sendto.ejb.interceptor.BundleResult;

/**
 * 
 * @author Lev Nadeinsky
 */
@Stateless
@LocalBean
public class SyncBean {
	
	@BundleResult
	public TimeSyncDto test(@Observes TimeSyncDto dto){
		
		long ts=System.currentTimeMillis();
		
		return dto.setServer(ts).setDelta(ts-dto.getClient());
	}
	
}
