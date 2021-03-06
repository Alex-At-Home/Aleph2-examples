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
package com.ikanow.aleph2.storm_harvest_technology;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.ikanow.aleph2.utils.JarBuilderUtil;

@SuppressWarnings("unused")
public class TestJarBuilderUtil {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException {
		Set<String> dirs_to_ignore = Sets.newHashSet("org/slf4j", "org/apache/log4j");
		List<String> jar_files_to_merge = new ArrayList<String>();
				
		//give sample harvest lib jar file
		jar_files_to_merge.add("sample_jar_files/sample_main.jar");
		//give sample jar file
		jar_files_to_merge.add("sample_jar_files/sample_lib.jar");		
				
		//merge both jars
		JarBuilderUtil.mergeJars(jar_files_to_merge, "sample_jar_files/output.jar", dirs_to_ignore);
		
		//these 2 steps are remotestorm controller -> submitjob()
		//submit jar to storm
		//submit topology to storm
		
		
		//assertTrue(JarBuilderUtil.createJar());
	}
//	
//	@Test
//	public void testMergingJars() throws IOException {
//		List<String> jar_files_to_merge = new ArrayList<String>();
//		String output_jar = "sample_jar_files/output.jar";
//		String main_jar = "sample_jar_files/sample_main.jar";
//		String lib_jar = "sample_jar_files/sample_lib.jar";
//		
//		//give sample harvest lib jar file
//		// /com/ikanow/aleph2/harvester/SomeLibraryClass.class
//		jar_files_to_merge.add(main_jar);
//		//give sample jar file
//		// /com/ikanow/aleph2/harvester/App.class
//		// /com/ikanow/aleph2/harvester/SomeLibraryClass.class
//		jar_files_to_merge.add(lib_jar);		
//				
//		//merge both jars
//		JarBuilderUtil.mergeJars(jar_files_to_merge, "sample_jar_files/output.jar");
//		
//		//test the output jar has the correct files
//		ZipFile zip_main = new ZipFile(main_jar);
//		ZipFile zip_lib = new ZipFile(lib_jar);		
//		ZipFile zip_output = new ZipFile(output_jar);
//
//		//Test App.class matches the lib entry
//		ZipEntry zip_entry_app_output = zip_output.getEntry("com/ikanow/aleph2/harvester/App.class");
//		ZipEntry zip_entry_app_lib = zip_lib.getEntry("com/ikanow/aleph2/harvester/App.class");
//		assertEquals(zip_entry_app_lib.getSize(), zip_entry_app_output.getSize());
//
//		//Test SomeLibraryClass.class matches the main entry (did not get overwritten by lib version)		
//		ZipEntry zip_entry_some_output = zip_output.getEntry("com/ikanow/aleph2/harvester/SomeLibraryClass.class");
//		ZipEntry zip_entry_some_main = zip_main.getEntry("com/ikanow/aleph2/harvester/SomeLibraryClass.class");
//		assertEquals(zip_entry_some_output.getSize(), zip_entry_some_main.getSize());
//		
//		zip_main.close();
//		zip_lib.close();
//		zip_output.close();
//	}

}
