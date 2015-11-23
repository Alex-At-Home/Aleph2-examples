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
package com.ikanow.aleph2.harvest.script.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import scala.Tuple2;

import com.ikanow.aleph2.data_model.interfaces.data_import.IHarvestContext;
import com.ikanow.aleph2.data_model.objects.data_import.DataBucketBean;
import com.ikanow.aleph2.data_model.utils.ErrorUtils;
import com.ikanow.aleph2.data_model.utils.Tuples;

/** Utilities for managing external processes
 * @author alex
 */
public class ProcessUtils {
	public static String PID_OUTPUT_DIR = "/opt/aleph2-home/run/";
	
	/** Launches a process, returns any error in _1(), the pid in _2()
	 * @param bucket
	 * @param context
	 * @return
	 */
	public static Tuple2<String, String> launchProcess(final ProcessBuilder process_to_launch, final DataBucketBean bucket) {
		try {
			// Try
//			final File run_dir = new File (PID_OUTPUT_DIR);
//			run_dir.mkdir();
//			if (!run_dir.canWrite()) {
//				throw new RuntimeException("Can't write to: " + run_dir);
//			}
			
			final Process px = process_to_launch.start();
			
			//logstash was slow to start up, so this made sense, but for quick scripts there is no reason
			//to expect a 5s turnaround
			// Sleep for 5s to see if it's going to die quickly
//			for (int i = 0; i < 5; ++i) {
//				try { Thread.sleep(1000L); } catch (Exception e) {}
//				if (!px.isAlive()) {
//					break;					
//				}
//			}
			
			String err = null;
			String pid = null;
			if (!px.isAlive()) {
				err = "Unknown error: " + px.exitValue() + ": " + 
						process_to_launch.command().stream().collect(Collectors.joining(" "));
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
	
//	public static String getPid(final DataBucketBean bucket) {
//		final File run_file = new File (PID_OUTPUT_DIR + bucket._id());
//		try (BufferedReader br = new BufferedReader(new FileReader(run_file))) {
//			return br.readLine();
//		}
//		catch (Exception e) {
//			return null;
//		}
//	}
	
	/** Stores the pid for this bucket
	 * @param bucket
	 * @param pid
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void storePid(final DataBucketBean bucket, String pid) throws FileNotFoundException, UnsupportedEncodingException {
//		final File run_file = new File (PID_OUTPUT_DIR + bucket._id());
//		try (PrintWriter writer = new PrintWriter(run_file, "UTF-8")) {
//			writer.println(pid);
//		}
	}
}
