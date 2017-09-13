/**
 * 
 */

package com.dpabpm.login.action;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;

/**
 * @author phucnv
 * @date Sep 9, 2017
 */
@Component(immediate = true, property = {
	"key=login.events.post"
}, service = LifecycleAction.class)
public class LoginPostAction implements LifecycleAction {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.liferay.portal.kernel.events.LifecycleAction#processLifecycleEvent(
	 * com.liferay.portal.kernel.events.LifecycleEvent)
	 */
	@Override
	public void processLifecycleEvent(LifecycleEvent lifecycleEvent)
		throws ActionException {

	}

}
