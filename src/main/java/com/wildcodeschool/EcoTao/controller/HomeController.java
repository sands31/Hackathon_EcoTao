package com.wildcodeschool.EcoTao.controller;



import java.util.ArrayList;
import java.util.List;

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

    List<ArrayList<String>> listGlobale = new ArrayList<ArrayList<String>>();
    List <String > list = new ArrayList<>();
	
	
	
	
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
	                  list.add(journey.get("co2_emission").get("value").toString());
						arrayNodeSections =  (ArrayNode) journey.get("sections");
						//arrayNodeCor = (ArrayNode) jsonNode.get("sections").get("geojson").get("coordinates");
						for(int i = 0  ; i < arrayNodeSections.size() ;i++ ) {
							 geoJson = arrayNodeSections.get(i).get("geojson");
							 
						     if(arrayNodeSections.get(i).get("type").toString().contains("street_network")) {
						    	 System.out.print(arrayNodeSections.get(i).get("mode") + " "); 
						    	 list.add(arrayNodeSections.get(i).get("mode").toString());
						    	 ArrayNode arrayNodeCoordStreet = (ArrayNode) geoJson.get("coordinates");
						    	 for(int j = 0; j < arrayNodeCoordStreet.size() ; j++) {
						    		   for(int k = 0 ; k < 2 ; k++) {
						    			  // System.out.print( arrayNodeCoordStreet.get(j).get(k) +" ---");
						    			   
						    		   }
						    		 //  System.out.println("");
						    	 }
//						    	 String[] temp;
//						    	 for (JsonNode coord : arrayNodeCoordStreet) {
//						    		 System.out.print(coord.get("1"));
//						    	 }
						    	 
						     }else if(arrayNodeSections.get(i).get("type").toString().contains("public_transport"))
						     {
						    	 System.out.print(arrayNodeSections.get(i).get("display_informations").get("physical_mode") + " ");
						    	 list.add(arrayNodeSections.get(i).get("display_informations").get("physical_mode").toString());
						    	 ArrayNode arrayNodeCoordPublic = (ArrayNode) geoJson.get("coordinates");
						    	// System.out.println(arrayNodeCoordPublic);
//						    	 for (JsonNode coord : arrayNodeCoordPublic) {
//						    		 System.out.print(coord.get("1"));
//						    	 }
						     }else {
						    	 System.out.print(arrayNodeSections.get(i).get("type") + " ");
						    	 list.add(arrayNodeSections.get(i).get("type").toString());
						    	
						     }
						     
						     
						}
			   System.out.println("");
	         	System.out.println("---------------------------------");
	         	listGlobale.add((ArrayList<String>) list);
	          }
	       }
		System.out.println(jsonObject.get("car_direct_path").get("co2_emission").get("value"));
		System.out.println("-------------------------------------");
		  for(List p:listGlobale)
	        {
	            for(int i=0;i<p.size();i++)
	               System.out.println("["+listGlobale.indexOf(p)+"]"+"["+ p.indexOf(p.get(i))+"]  ="+p.get(i) );
	         
	        }
	
		
		
		return jsonObject;
	}
}
