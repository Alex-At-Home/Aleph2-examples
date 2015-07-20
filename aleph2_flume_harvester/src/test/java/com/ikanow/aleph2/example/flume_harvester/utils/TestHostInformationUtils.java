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

import static org.junit.Assert.*;

import org.junit.Test;

public class TestHostInformationUtils {

	@Test
	public void testUuidGenerator() {
		
		assertEquals(HostInformationUtils.getProcessUuid(), HostInformationUtils.getProcessUuid());
	}
}
