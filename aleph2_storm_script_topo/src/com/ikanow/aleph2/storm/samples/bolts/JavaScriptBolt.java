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
package com.ikanow.aleph2.storm.samples.bolts;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.ikanow.aleph2.storm.samples.script.CompiledScriptFactory;
import com.ikanow.aleph2.storm.samples.script.NoSecurityManager;
import com.ikanow.aleph2.storm.samples.script.PropertyBasedScriptProvider;

public class JavaScriptBolt extends BaseRichBolt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -17206092588932701L;
	private OutputCollector _collector;	
	protected transient CompiledScriptFactory compiledScriptFactory = null;
	private String propertyFileName;
	
	protected static String CHECKCALL = "check();";
	
	public JavaScriptBolt(String propertyFileName){		
		
		this.propertyFileName  = propertyFileName;
		
	}
	
	protected CompiledScriptFactory getCompiledScriptFactory(){
		if(compiledScriptFactory == null){
			this.compiledScriptFactory = new CompiledScriptFactory(new PropertyBasedScriptProvider(propertyFileName){
				@Override
				public void init(String propertyFile) {
					super.init(propertyFile);
					scriptlets.add(CHECKCALL);
				}
				
			}, new NoSecurityManager());

			compiledScriptFactory.executeCompiledScript(CompiledScriptFactory.GLOBAL);
		}
		return compiledScriptFactory;
	}
	
	@Override
	public void execute(Tuple tuple) {
		String ipLine = tuple.getString(0);
		Object retVal = getCompiledScriptFactory().executeCompiledScript(CHECKCALL,"_ip",ipLine);
		
		/*while ( matcher.find() ) {
			_collector.emit(tuple, new Values( matcher.group(0).trim()));
		} */		
				
		//always ack the tuple to acknowledge we've processed it, otherwise a fail message will be reported back
		//to the spout
		_collector.ack(tuple); 
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}
}
