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
package com.ikanow.aleph2.web_utils;

import com.google.inject.Inject;
import com.ikanow.aleph2.data_model.interfaces.shared_services.IServiceContext;

public class DefaultInjectorContainer {

	
	@Inject
	private IServiceContext serviceContext;

	public void setServiceContext(IServiceContext serviceContext) {
		this.serviceContext = serviceContext;
	}

	public IServiceContext getServiceContext() {
		return serviceContext;
	}

	public DefaultInjectorContainer(){
		
	}
	
}
