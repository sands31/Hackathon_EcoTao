package com.wildcodeschool.EcoTao.controller;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;




import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;

import reactor.core.publisher.Mono;


@Controller
public class HomeController {


	
	
	
	
	private String url = "https://api.navitia.io/v1";
	private String token ="a3653e1d-06a1-4edc-b768-c9bd561d3251";
	private JsonNode jsonObject, geoJson  ;
	private  ArrayNode arrayNodeJourneys,arrayNodeSections;
	ObjectMapper objectMapper = new ObjectMapper();
	

	@GetMapping("/hello")
	@ResponseBody
    public JsonNode toHome() {
		WebClient  webclient = WebClient.create(url);
		Mono<String> call = webclient.get()
                        .uri(uriBuilder-> uriBuilder
                        		.path("/coverage/{couverage}/journeys")
                        		.queryParam("from","1.92587;47.82710")
                        		.queryParam("to", "1.90518;47.90639")
                        		.queryParam("traveler_type", "standard")
                        		
                        		.build("fr-cen"))		
		          .headers(headers -> headers.setBasicAuth(token, ""))
		          .retrieve()
		          .bodyToMono(String.class);
		String response = call.block();
		
		try {
			jsonObject = objectMapper.readTree(response).get("context");
		    arrayNodeJourneys = (ArrayNode) objectMapper.readTree(response).get("journeys");
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	      if(arrayNodeJourneys.isArray()) {
	          for(JsonNode journey : arrayNodeJourneys) {
	             System.out.print(journey.get("co2_emission").get("value") +" ");
	           
						arrayNodeSections =  (ArrayNode) journey.get("sections");
						//arrayNodeCor = (ArrayNode) jsonNode.get("sections").get("geojson").get("coordinates");
						for(JsonNode section : arrayNodeSections) {
							 
							 geoJson = section.get("geojson");
							 
						     if(section.get("type").toString().contains("street_network")) {
						    	 System.out.print(section.get("mode") + " "); 
						    	 ArrayNode arrayNodeCoordStreet = (ArrayNode) geoJson.get("coordinates");
						    	 System.out.println(arrayNodeCoordStreet);
//						    	 String[] temp;
//						    	 for (JsonNode coord : arrayNodeCoordStreet) {
//						    		 System.out.print(coord.get("1"));
//						    	 }
						    	 
						     }else if(section.get("type").toString().contains("public_transport"))
						     {
						    	 System.out.print(section.get("display_informations").get("physical_mode") + " ");
						    	 ArrayNode arrayNodeCoordPublic = (ArrayNode) geoJson.get("coordinates");
						    	 System.out.println(arrayNodeCoordPublic);
//						    	 for (JsonNode coord : arrayNodeCoordPublic) {
//						    		 System.out.print(coord.get("1"));
//						    	 }
						     }else {
						    	 System.out.print(section.get("type") + " ");
						    	
						     }
						     
						     
						}
			   System.out.println("");
	         	System.out.println("---------------------------------");
	          }
	       }
		System.out.println(jsonObject.get("car_direct_path").get("co2_emission").get("value"));
	
		
		return jsonObject;
	}
}
