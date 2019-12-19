package com.wildcodeschool.EcoTao.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;

import reactor.core.publisher.Mono;


@Controller
public class HomeController {

    List<ArrayList<String>> listGlobale = new ArrayList<ArrayList<String>>();
   
	
	
	
	
	private String url = "https://api.navitia.io/v1";
	private String token ="a3653e1d-06a1-4edc-b768-c9bd561d3251";
	private String from ="1.95814;47.92060";
	private String to = "1.90518;47.90639";
	private JsonNode jsonObject, geoJson  ;
	private  ArrayNode arrayNodeJourneys,arrayNodeSections;
	ObjectMapper objectMapper = new ObjectMapper();
	

	@GetMapping("/request")
    public String toHome() {
		WebClient  webclient = WebClient.create(url);
		Mono<String> call = webclient.get()
                        .uri(uriBuilder-> uriBuilder
                        		.path("/coverage/{couverage}/journeys")
                        		.queryParam("from",from)
                        		.queryParam("to", to )
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
	        	  List <String > list = new ArrayList<>();
	             System.out.print(journey.get("co2_emission").get("value") +" ");
	                  list.add(journey.get("co2_emission").get("value").toString().replace("\"", ""));
						arrayNodeSections =  (ArrayNode) journey.get("sections");
						//arrayNodeCor = (ArrayNode) jsonNode.get("sections").get("geojson").get("coordinates");
						for(int i = 0  ; i < arrayNodeSections.size() ;i++ ) {
							 geoJson = arrayNodeSections.get(i).get("geojson");
							 
						     if(arrayNodeSections.get(i).get("type").toString().contains("street_network")) {
						    	 System.out.print(arrayNodeSections.get(i).get("mode") + " "); 
						    	 list.add(arrayNodeSections.get(i).get("mode").toString().replace("\"", ""));
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
						    	 list.add(arrayNodeSections.get(i).get("display_informations").get("physical_mode").toString().replace("\"", ""));
						    	 ArrayNode arrayNodeCoordPublic = (ArrayNode) geoJson.get("coordinates");
						    	// System.out.println(arrayNodeCoordPublic);
//						    	 for (JsonNode coord : arrayNodeCoordPublic) {
//						    		 System.out.print(coord.get("1"));
//						    	 }
						     }else {
						    	 System.out.print(arrayNodeSections.get(i).get("type") + " ");
						    	 list.add(arrayNodeSections.get(i).get("type").toString().replace("\"", ""));
						    	
						     }
						     
						     
						}
			   System.out.println("");
	         	System.out.println("---------------------------------");
	         	listGlobale.add((ArrayList<String>) list);
	          }
	       }
		System.out.println(jsonObject.get("car_direct_path").get("co2_emission").get("value"));
		System.out.println("-------------------------------------");
		  for(int i = 0 ; i< listGlobale.size() ; i ++)
	        {    
	            for(int j=0; j  < listGlobale.get(i).size();j++) {
	       
	            	System.out.print(listGlobale.get(i).get(j) +"  " );	
	            }
	            System.out.println(" ");       	         
	        }
		  return "redirect:/recherche";
	}
	
	@GetMapping("/test")
	public String test(Model model) {
		
		model.addAttribute("journeys", listGlobale);
		return "test";
		
	}
	@GetMapping("/map")
	public String shoMap(Model model) {
		  String longFrom = "" ;
		  String latFrom = "";
		  String longTo = "" ;
		  String latTo = "";
		   longFrom = from.substring(0, 7);
		   latFrom = from.substring(8,from.length());
		   longTo = to.substring(0, 7);
		   latTo = to.substring(8,from.length()); 
		     
		 model.addAttribute("longFrom", longFrom );
		 model.addAttribute("latFrom", latFrom );
		 model.addAttribute("longTo", longTo );
		 model.addAttribute("latTo", latTo );
		 
		
		return "leaflet";
		
	}
	

	
	@GetMapping("/recherche")
	public String recherche(Model model) {
		
		model.addAttribute("journeys", listGlobale);
		return "recherche";
		
	}

}
