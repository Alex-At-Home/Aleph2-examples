/*******************************************************************************
 * Copyright 2016, The IKANOW Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ikanow.aleph2.web_login;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;

import com.google.inject.Injector;
import com.ikanow.aleph2.data_model.utils.ModuleUtils;
import com.ikanow.aleph2.security.module.CoreSecurityModule;


public class WebEnvironment extends IniWebEnvironment{

	private static final Logger logger = LogManager.getLogger();


	protected Collection<Realm> realms = null;

    protected void configure() {
    	
		try {
			Injector injector = ModuleUtils.getAppInjector().get();			
			this.realms = injector.getInstance(CoreSecurityModule.realmCollectionKey());
			//logger.debug("Realm:"+realms);
		} catch (Throwable e) {
			logger.error("Caught exception getting app injector:",e);
		}

    	super.configure();
        
    }

	@Override
	protected WebSecurityManager createWebSecurityManager() {
		WebSecurityManager webSecurityManager = super.createWebSecurityManager();
		if(webSecurityManager instanceof DefaultWebSecurityManager){
			DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager)webSecurityManager;
			securityManager.setRealms(realms);
		}
		return webSecurityManager;
	}

	
	
	
}
