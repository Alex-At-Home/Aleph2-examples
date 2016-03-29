/*******************************************************************************
 * Copyright 2015, The IKANOW Open Source Project.
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
package com.ikanow.aleph2.security.shiro;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;



/**
 * This class checks is used for mock logins for testing purposes. IT matches true if the password is the same as the login.
 * @author jfreydank
 *
 */
public class Login123CredentialMatcher extends SimpleCredentialsMatcher {
	private static final Logger logger = LogManager.getLogger(ActiveSessionCredentialMatcher.class);

	
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    	 try {
    	    
             String user = (String)token.getPrincipal();    	 
             String password = (String)token.getCredentials();    	 
    		 return (user+"123").equals(password);
		} catch (Exception e) {
			logger.error("Caught error matching credentials:"+e.getMessage(),e);
		
    	}
        return false;
    }	
}