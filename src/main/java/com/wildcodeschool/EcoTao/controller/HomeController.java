package com.wildcodeschool.EcoTao.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;

import reactor.core.publisher.Mono;

@Controller
public class HomeController {

	List<ArrayList<String>> listGlobale = new ArrayList<ArrayList<String>>();
	List<ArrayList<String>> listGlobalePath = new ArrayList<ArrayList<String>>();
	Double matrice[][];

	private String url = "https://api.navitia.io/v1";
	private String token = "a3653e1d-06a1-4edc-b768-c9bd561d3251";

	private String from = "1.90089;47.86403";
	private String to = "1.90459;47.89471";
	private JsonNode jsonObject, geoJson;
	private ArrayNode arrayNodeJourneys, arrayNodeSections;
	private double carCo2;
	ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/request")
	public String toHome() {
		this.listGlobale.clear();
		this.carCo2 = 0;
		WebClient webclient = WebClient.create(url);
		Mono<String> call = webclient.get()
				.uri(uriBuilder -> uriBuilder.path("/coverage/{couverage}/journeys").queryParam("from", from)
						.queryParam("to", to).queryParam("traveler_type", "standard")

						.build("fr-cen"))
				.headers(headers -> headers.setBasicAuth(token, "")).retrieve().bodyToMono(String.class);
		String response = call.block();

		try {
			jsonObject = objectMapper.readTree(response).get("context");
			arrayNodeJourneys = (ArrayNode) objectMapper.readTree(response).get("journeys");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		carCo2 = Double.parseDouble(jsonObject.get("car_direct_path").get("co2_emission").get("value").toString());

		if (arrayNodeJourneys.isArray()) {
			for (JsonNode journey : arrayNodeJourneys) {
				List<String> list = new ArrayList<>();
				List<String> pathList = new ArrayList<>();
				double co2 = Double.parseDouble(journey.get("co2_emission").get("value").toString().replace("\"", ""));
				double ecoCo2 = carCo2 - co2;

				if (ecoCo2 >= 0) {

					list.add((int) ecoCo2 + " gEC");

					arrayNodeSections = (ArrayNode) journey.get("sections");
					// arrayNodeCor = (ArrayNode)
					// jsonNode.get("sections").get("geojson").get("coordinates");
					for (int i = 0; i < arrayNodeSections.size(); i++) {
						geoJson = arrayNodeSections.get(i).get("geojson");

						if (arrayNodeSections.get(i).get("type").toString().contains("street_network")) {
							list.add(arrayNodeSections.get(i).get("mode").toString().replace("\"", ""));
							ArrayNode arrayNodeCoordStreet = (ArrayNode) geoJson.get("coordinates");
							for (int j = 0; j < arrayNodeCoordStreet.size(); j++) {
								for (int k = 0; k < 2; k++) {
									pathList.add(arrayNodeCoordStreet.get(j).get(k).toString());

								}
								System.out.println("");
							}
//						    	 String[] temp;
//						    	 for (JsonNode coord : arrayNodeCoordStreet) {
//						    		 System.out.print(coord.get("1"));
//						    	 }

						} else if (arrayNodeSections.get(i).get("type").toString().contains("public_transport")) {

							list.add(arrayNodeSections.get(i).get("display_informations").get("physical_mode")
									.toString().replace("\"", ""));
							ArrayNode arrayNodeCoordPublic = (ArrayNode) geoJson.get("coordinates");
							for (int j = 0; j < arrayNodeCoordPublic.size(); j++) {
								for (int k = 0; k < 2; k++) {
									pathList.add(arrayNodeCoordPublic.get(j).get(k).toString());
								}
							}
						} else {
							list.add(arrayNodeSections.get(i).get("type").toString().replace("\"", ""));
						}

					}

					double temps = Double.parseDouble(journey.get("duration").toString().replace("\"", "")) / 60;
					String tempsString = (int) temps + "min";
					list.add(tempsString);

					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).contains("bss") || list.get(i).contains("waiting")
								|| list.get(i).contains("demand") || list.get(i).contains("transfer"))
							list.remove(i);
					}
					
//					Comparator <String> comparator = new Comparator <String> () {
//						@Override
//						public int compare(String string1, String string2) {
//							return string1.compareTo(string2);
//						}
//					};
//					Collections.sort(listGlobale, comparator);

					listGlobale.add((ArrayList<String>) list);
					listGlobalePath.add((ArrayList<String>) pathList);
				}

			}
		}


		for (int i = 0; i < 1; i++) {

			matrice = new Double[listGlobalePath.get(i).size() / 2][2];
			int k = 0;
			for (int j = 0; j < listGlobalePath.get(i).size() - 1; j += 2) {

				matrice[k][1] = Double.parseDouble(listGlobalePath.get(i).get(j));
				matrice[k][0] = Double.parseDouble(listGlobalePath.get(i).get(j + 1));
				k++;

			}
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
		

		String longFrom = "";
		String latFrom = "";
		String longTo = "";
		String latTo = "";
		longFrom = from.substring(0, 7);
		latFrom = from.substring(8, from.length());
		longTo = to.substring(0, 7);
		latTo = to.substring(8, from.length());

		model.addAttribute("longFrom", longFrom);
		model.addAttribute("latFrom", latFrom);
		model.addAttribute("longTo", longTo);
		model.addAttribute("latTo", latTo);

		String matriceStr = new String("[");

		for (int i = 0; i < matrice.length; i++) {
			matriceStr += "[";

			for (int j = 0; j < matrice[0].length; j++) {
				matriceStr += matrice[i][j];
				if (j != matrice[0].length - 1)
					matriceStr += ",";
			}
			matriceStr += "]";
			if (i != matrice.length - 1)
				matriceStr += ",";
		}

		matriceStr += "]";

		model.addAttribute("matrix", matriceStr);

		return "leaflet";

	}
	
	@GetMapping("/mapEmpty")
	public String showMapEmpty() {

		
		return "map.html";

	}

	@GetMapping("/recherche")
	public String recherche(Model model) {

		model.addAttribute("journeys", listGlobale);
		return "recherche";

	}
	
	@PostMapping("/showPath")
	public String showMap(Model model , @RequestParam int index) {
		
		
			matrice = new Double[listGlobalePath.get(index).size() / 2][2];
			int k = 0;
			for (int j = 0; j < listGlobalePath.get(index).size() - 1; j += 2) {

				matrice[k][1] = Double.parseDouble(listGlobalePath.get(index).get(j));
				matrice[k][0] = Double.parseDouble(listGlobalePath.get(index).get(j + 1));
				k++;

			}

			String longFrom = "";
			String latFrom = "";
			String longTo = "";
			String latTo = "";
			longFrom = from.substring(0, 7);
			latFrom = from.substring(8, from.length());
			longTo = to.substring(0, 7);
			latTo = to.substring(8, from.length());

			model.addAttribute("longFrom", longFrom);
			model.addAttribute("latFrom", latFrom);
			model.addAttribute("longTo", longTo);
			model.addAttribute("latTo", latTo);

			String matriceStr = new String("[");

			for (int i = 0; i < matrice.length; i++) {
				matriceStr += "[";

				for (int j = 0; j < matrice[0].length; j++) {
					matriceStr += matrice[i][j];
					if (j != matrice[0].length - 1)
						matriceStr += ",";
				}
				matriceStr += "]";
				if (i != matrice.length - 1)
					matriceStr += ",";
			}

			matriceStr += "]";
			model.addAttribute("matrix", matriceStr);
		
		
		return "leaflet";
	}

}
