/**
 * 
 */
package com.duowan.gamebox.msgcenter.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author zhangtao.robin
 * 
 */
@ApplicationPath("/")
public class MsgCenterApplication extends Application {

	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(ShortRequestService.class);
		s.add(ServerListService.class);
		s.add(PojoJacksonJsonProvider.class);
		return s;
	}

}
