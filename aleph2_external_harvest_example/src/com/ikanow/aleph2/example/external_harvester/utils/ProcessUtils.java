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
package com.ikanow.aleph2.example.external_harvester.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import scala.Tuple2;

import com.ikanow.aleph2.data_model.interfaces.data_import.IHarvestContext;
import com.ikanow.aleph2.data_model.objects.data_import.DataBucketBean;
import com.ikanow.aleph2.data_model.utils.ErrorUtils;
import com.ikanow.aleph2.data_model.utils.Tuples;

/** Utilities for managing external processes
 * @author alex
 */
public class ProcessUtils {

	/** Launches a process, returns any error in _1(), the pid in _2()
	 * @param bucket
	 * @param context
	 * @return
	 */
	public static Tuple2<String, String> launchProcess(final DataBucketBean bucket, final IHarvestContext context) {
		try {
			// Try
			final File run_dir = new File ("/opt/aleph2-home/run/");
			run_dir.mkdir();
			if (!run_dir.canWrite()) {
				throw new RuntimeException("Can't write to: " + run_dir);
			}
			
			final String classpath = Stream.concat(
					context.getHarvestContextLibraries(Optional.empty()).stream(),
					context.getHarvestLibraries(Optional.of(bucket)).get().values().stream()
					)
					.collect(Collectors.joining(":"))
					;
			
			ProcessBuilder pb = new ProcessBuilder(
					Arrays.<String>asList(
							Optional.ofNullable(System.getenv("JAVA_HOME")).orElse("/usr/")  
								+ File.separator + "bin" + File.separator + "java",
							"-classpath",
							classpath,
							"com.ikanow.aleph2.example.external_harvester.services.ExternalProcessLaunchService",
							context.getHarvestContextSignature(Optional.of(bucket), Optional.empty()),
							"test_argument"
							))
			.redirectErrorStream(true)
			.redirectOutput(new File("/dev/null")) // (just ignore stdout/stderr for this simple example)
			//.redirectOutput(new File("/tmp/external_harvest_test.txt"))
			;
			
			final Process px = pb.start();
			
			// Sleep for 5s to see if it's going to die quickly
			for (int i = 0; i < 5; ++i) {
				try { Thread.sleep(1000L); } catch (Exception e) {}
				if (!px.isAlive()) {
					break;					
				}
			}
			
			String err = null;
			String pid = null;
			if (!px.isAlive()) {
				err = "Unknown error: " + px.exitValue() + ": " + 
						pb.command().stream().collect(Collectors.joining(" "));
					// (since not capturing output)
			}
			else {
				pid = getPid(px);
				storePid(bucket, pid);
			}
			return Tuples._2T(err, pid);
		}
		catch (Throwable t) {
			return Tuples._2T(ErrorUtils.getLongForm("{0}", t), null);
		}
	}
	
	/** Kills the specified process
	 * @param pid
	 */
	public static Tuple2<String,Boolean> killProcess(final String pid) {
		try {
			if (!isRunning(pid)) {
				return Tuples._2T("(process " + pid + " already deleted)", true);
			}
			final Process px = new ProcessBuilder(Arrays.asList("kill", "-15", pid))
			//.redirectErrorStream(true).inheritIO()
									.start()
			;
			for (int i = 0; i < 5; ++i) {
				try { Thread.sleep(1000L); } catch (Exception e) {}
				if (!px.isAlive()) {
					break;					
				}
			}
			if (!px.isAlive()) {
				return Tuples._2T("Tried to kill " + pid + ": success = " + px.exitValue(), 0 == px.exitValue());
			}
			else {
				return Tuples._2T("Timed out trying to kill: " + pid, true);				
			}
		}
		catch (Throwable t) {//(do nothing)
			return Tuples._2T("Kill failed: " + pid + ": " + ErrorUtils.getLongForm("{0}", t), true);
		}		
	}
	
	/** Check if a process is running
	 * @param pid
	 * @return
	 * @throws IOException 
	 */
	public static boolean isRunning(final String pid) throws IOException {
		return new File("/proc/" + pid).exists();
	}
	
	/** Get the pid from the process (MAY NOT WORK ON WINDOWS)
	 * @param px
	 * @return
	 */
	public static String getPid(final Process px) {
	    try {
	        final Class<?> ProcessImpl = px.getClass();
	        final Field field = ProcessImpl.getDeclaredField("pid");
	        field.setAccessible(true);
	        return Integer.toString(field.getInt(px));
	    } 
	    catch (Throwable t) {
	        return "unknown";
	    }		
	}
	
	public static String getPid(final DataBucketBean bucket) {
		final File run_file = new File ("/opt/aleph2-home/run/" + bucket._id());
		try (BufferedReader br = new BufferedReader(new FileReader(run_file))) {
			return br.readLine();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/** Stores the pid for this bucket
	 * @param bucket
	 * @param pid
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void storePid(final DataBucketBean bucket, String pid) throws FileNotFoundException, UnsupportedEncodingException {
		final File run_file = new File ("/opt/aleph2-home/run/" + bucket._id());
		try (PrintWriter writer = new PrintWriter(run_file, "UTF-8")) {
			writer.println(pid);
		}
	}
}
