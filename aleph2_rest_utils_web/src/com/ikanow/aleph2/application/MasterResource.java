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
package com.ikanow.aleph2.application;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.ikanow.aleph2.aleph2_rest_utils.FileDescriptor;
import com.ikanow.aleph2.aleph2_rest_utils.RestCrudFunctions;
import com.ikanow.aleph2.aleph2_rest_utils.RestCrudFunctions.FunctionType;
import com.ikanow.aleph2.aleph2_rest_utils.RestUtils;
import com.ikanow.aleph2.data_model.interfaces.shared_services.ICrudService;
import com.ikanow.aleph2.data_model.interfaces.shared_services.IServiceContext;
import com.ikanow.aleph2.data_model.objects.data_import.DataBucketBean;
import com.ikanow.aleph2.data_model.utils.ErrorUtils;

@Path("/aleph2_api")
public class MasterResource {
	final IServiceContext service_context;
	final Logger _logger = LogManager.getLogger();
	
	public MasterResource(final IServiceContext service_context) {
		this.service_context = service_context;
	}
	
	
	////////////////////////////////////READ METHODS//////////////////////////////////////
	//read w/ query in body GET call (most services don't allow body but crud w3c does I think)
	@GET
	@Path("/{service_name}/{read_write}/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@QueryParam("buckets") String buckets,
			@QueryParam("limit") Long limit,
			String query_json) {
		return get(service_name, read_write, identifier, null, buckets, limit, query_json);		
	}
	
	//read w/ query in body POST call (circumventing get with body issues)
	@POST
	@Path("/query/{service_name}/{read_write}/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQuery(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@QueryParam("buckets") String buckets,
			@QueryParam("limit") Long limit,
			String query_json) {
		return get(service_name, read_write, identifier, null, buckets, limit, query_json);
	}
	
	//read by id
	@GET
	@Path("/{service_name}/{read_write}/{identifier}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@PathParam("id") String id,
			@QueryParam("buckets") String buckets,
			@QueryParam("limit") Long limit) {
		return get(service_name, read_write, identifier, null, buckets, limit, null);
	}
	
	//helper function for all read requests
	private Response get(
			String service_name,
			String read_write,
			String identifier,
			String id,
			String buckets,
			Long limit,
			String query_json) {
		_logger.error("READ QUERY request: sn: " + service_name +
				" rw: " + read_write +
				" i: " + identifier + 
				" id: " + id +
				" b: " + buckets +
				" q: " + query_json +
				" l: " + limit
				);
		return RestCrudFunctions.readFunction(service_context, FunctionType.QUERY, service_name, read_write, 
				identifier, Optional.ofNullable(buckets), 
				RestUtils.getOptional(query_json), Optional.ofNullable(id), 
				RestUtils.getOptional(limit));
	}
	
	//count by query (body of GET request)
	@GET
	@Path("/count/{service_name}/{read_write}/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCount(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@QueryParam("buckets") String buckets,
			String query_json) {
		return postCount(service_name, read_write, identifier, buckets, query_json);		
	}
	
	//count by query (body of POST request)
	@POST
	@Path("/count/{service_name}/{read_write}/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response postCount(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@QueryParam("buckets") String buckets,
			String query_json) {
		_logger.error("READ COUNT request: sn: " + service_name +
				" rw: " + read_write +
				" i: " + identifier + 
				" b: " + buckets +
				" q: " + query_json
				);
		return RestCrudFunctions.readFunction(service_context, FunctionType.COUNT, service_name, read_write, identifier, Optional.ofNullable(buckets), RestUtils.getOptional(query_json), Optional.empty(), Optional.empty());
	}
	
	
	////////////////////////////////////CREATE METHODS//////////////////////////////////////
	//regular json post
	@POST
	@Path("/{service_name}/{read_write}/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response post(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@QueryParam("buckets") String buckets,
			String json) {
		_logger.error("READ COUNT request: sn: " + service_name +
				" rw: " + read_write +
				" i: " + identifier + 
				" b: " + buckets +
				" j: " + json
				);
		return RestCrudFunctions.createFunction(service_context, service_name, read_write, identifier, Optional.ofNullable(buckets), RestUtils.getOptional(json));
	}
	
	//file + (maybe) json post
	//this is specifically for 2 end points:
	//management_db | write | bucket file uploads (no json)
	//management_db | write | shared lib uploads (json)
	@POST
	@Path("/upload/{service_name}/{read_write}/{identifier}")
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces(MediaType.TEXT_PLAIN)
	public String post(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@FormDataParam("entry") String entry, 
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@QueryParam("bucket") String bucket_full_name) {		
		_logger.error("param: " + service_name + " input: " + entry);
		try {
			final JsonNode json = RestUtils.convertStringToJson(entry);
			//save file to storage service somewhere
			final DataBucketBean bucket = RestUtils.convertToBucketFullName(service_context, bucket_full_name);
			final ICrudService<FileDescriptor> crud_service = RestUtils.getBucketDataStore(this.service_context, bucket);			
			CompletableFuture<Supplier<Object>> fut = crud_service.storeObject(new FileDescriptor(uploadedInputStream, fileDetail.getFileName()));
			String res = fut.handle((ok, ex) -> {
				if ( ok != null )
					return "ok";
				else if ( ex != null) 
					return ErrorUtils.getLongForm("Exception storing object: {0}",ex);
				else
					return "something broke";
			}).get();
			
			return "hello" + json.toString() + " bucket is: " + bucket_full_name + " file is: " + fileDetail.getSize() + fileDetail.getFileName() + " result: " + res;
		} catch (Exception e) {
			return ErrorUtils.getLongForm("Error: {0}", e);
		}
	}
	
	////////////////////////////////////UPDATE METHODS//////////////////////////////////////
	//regular update request
	@PUT
	@Path("/{service_name}/{read_write}/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@QueryParam("buckets") String buckets,
			String json) {
		_logger.error("READ COUNT request: sn: " + service_name +
				" rw: " + read_write +
				" i: " + identifier + 
				" b: " + buckets +
				" j: " + json
				);
		return RestCrudFunctions.updateFunction(service_context, service_name, read_write, identifier, Optional.ofNullable(buckets), RestUtils.getOptional(json));
	}
	
	////////////////////////////////////DELETE METHODS//////////////////////////////////////
	//delete by query
	@DELETE
	@Path("/{service_name}/{read_write}/{identifier}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@QueryParam("buckets") String buckets,
			String query_json) {
		return delete(service_name, read_write, identifier, null, buckets, query_json);		
	}
	
	//delete by id
	@DELETE
	@Path("/{service_name}/{read_write}/{identifier}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteID(
			@PathParam("service_name") String service_name,
			@PathParam("read_write") String read_write,
			@PathParam("identifier") String identifier,
			@PathParam("id") String id,
			@QueryParam("buckets") String buckets) {
		return delete(service_name, read_write, identifier, null, buckets, null);
	}
	
	//delete helper function
	private Response delete(
			String service_name,
			String read_write,
			String identifier,
			String id,
			String buckets,
			String query_json) {
		_logger.error("DELETE QUERY request: sn: " + service_name +
				" rw: " + read_write +
				" i: " + identifier + 
				" id: " + id +
				" b: " + buckets +
				" q: " + query_json
				);
		return RestCrudFunctions.deleteFunction(service_context, service_name, read_write, identifier, Optional.ofNullable(buckets), RestUtils.getOptional(query_json), Optional.ofNullable(id));
	}
	
	
}
