/*******************************************************************************
* Copyright 2015, The IKANOW Open Source Project.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License, version 3,
* as published by the Free Software Foundation.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
* 
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
package com.ikanow.aleph2.example.flume_harvester.utils;

import java.net.InetAddress;

import com.ikanow.aleph2.data_model.utils.UuidUtils;

/** Provides static information about the host and process
 * @author acp
 *
 */
public class HostInformationUtils {

	private static String _hostname = null;
	private final static String _uuid;
	static {
		_uuid = UuidUtils.get().getRandomUuid();
	}
	
	/** Returns a UUID unique to this process
	 * @return the UUID (type 1)
	 */
	public static String getProcessUuid() {
		return _uuid;
	}
	
	/** Returns the hostname
	 * @return
	 */
	public static String getHostname() {
		// (just get the hostname once)
		if (null == _hostname) {
			try {
				_hostname = InetAddress.getLocalHost().getHostName();
			} catch (Exception e) {
				_hostname = "UNKNOWN";
			}
		}		
		return _hostname;
	}//TESTED		
}
