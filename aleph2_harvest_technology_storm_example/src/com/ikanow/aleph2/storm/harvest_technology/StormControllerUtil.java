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
******************************************************************************/
package com.ikanow.aleph2.storm.harvest_technology;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Factory for returning a local or remote storm controller
 * 
 * @author Burch
 *
 */
public class StormControllerUtil {
	public static IStormController getLocalStormController() {
		return new LocalStormController();
	}
	
	public static IStormController getRemoteStormController(@NonNull String nimbus_host, @NonNull int nimbus_thrift_port, @NonNull String storm_thrift_transport_plugin) {
		return new RemoteStormController(nimbus_host, nimbus_thrift_port, storm_thrift_transport_plugin);
	}
}
