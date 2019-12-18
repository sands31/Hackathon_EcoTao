package examples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class HackloweenController {
	
	private static final String HACKLOWEEN_URL = "https://hackathon-wild-hackoween.herokuapp.com";
	private Movie movie1 = movie(33);
	private Movie movie2 = movie(17);
	private Movie movie3 = movie(75);
	private Movie movie4 = movie(43);
	private Fighter you = new Fighter("Vous", 15);
	
	//Fight 1
	private Monster monster1 = monster(19);
	private Monster monster2 = monster(8);
	private Monster monster3 = monster(7);
	private Fighter fighter1 = new Fighter(monster1.getName(), monster1.getAttack());
	private Fighter fighter2 = new Fighter(monster2.getName(), monster2.getAttack());
	private Fighter fighter3 = new Fighter(monster3.getName(), monster3.getAttack());
	private Fighter[] fighters = {fighter1, fighter2, fighter3};
	private Monster[] monsters = {monster1, monster2, monster3};
	
	//Fight2
	private Monster monster4 = monster(13);
	private Monster monster5 = monster(1);
	private Monster monster6 = monster(20);
	private Fighter fighter4 = new Fighter(monster4.getName(), monster4.getAttack());
	private Fighter fighter5 = new Fighter(monster5.getName(), monster5.getAttack());
	private Fighter fighter6 = new Fighter(monster6.getName(), monster6.getAttack());
	private Fighter[] fighters2 = {fighter4, fighter5, fighter6};
	private Monster[] monsters2 = {monster4, monster5, monster6};

    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/quizz1")
    public String quizz1(Model model) {
    	model.addAttribute("movie1", movie1);
        return "quizz1";
    }
    
    @GetMapping("/answerQuizz1")
    public String answerQuizz1(Model model,
    		@RequestParam(value="answer1", required=false) String answer1,
    		@RequestParam(value="answer2", required=false) String answer2,
    		@RequestParam(value="answer3", required=false) String answer3) {
    	
    	if (answer1.equals("2") && answer2.equals("1") && answer3.equals("2")) {
    		String response= "Vous avez survécu !";
    		model.addAttribute("response", response);
    		return "quizz1won";
    	} else {
    		return "loose";
    	}   	       
    }
    
    @GetMapping("/fight1Choice")
    public String fight1Choice(Model model) {
    	model.addAttribute("monster1", monster1);
    	model.addAttribute("monster2", monster2);
    	model.addAttribute("monster3", monster3);
    	
    	
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	
    	return "fight1Choice";	
    }
    
    @GetMapping("/fight1/{num}")
    public String fight1(@PathVariable int num, Model model) {
  
      	//Reset life
    	you.setLife(100);
    	for (int i=0; i<3; i++) {
	    	fighters[i].setLife(100);
    	}
    	
    	//Fight
		ArrayList<String> fightInfos = new ArrayList<String>();
		String linkState3 = new String();
		fightInfos.add("Le combat a commencé !");
		while (fighters[num -1].getLife() > 0 && you.getLife() > 0) {
			fightInfos.add(fighters[num -1].takeHit(you));		
			if (fighters[num -1].getLife() > 0) {
				fightInfos.add(you.takeHit(fighters[num -1]));
			}
		}
		if (you.getLife() > 0) {
			fightInfos.add("Vous avez survécu !");
			linkState3  = "/quizz2";
		} else {
			fightInfos.add("Vous n'avez pas survécu...");
			linkState3  = "/loose1";
		}
		
		model.addAttribute("linkState3", linkState3);
		model.addAttribute("fightResult", fightInfos);
    	model.addAttribute("monster", monsters[num -1]);
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	
        return "fight1";
    }
    
    
    
    @GetMapping("/quizz2")
    public String quizz2(Model model) {
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
        return "quizz2";
    }
    
    @GetMapping("/answerQuizz2")
    public String answerQuizz2(Model model,
    		@RequestParam(value="answer1", required=false) String answer1,
    		@RequestParam(value="answer2", required=false) String answer2,
    		@RequestParam(value="answer3", required=false) String answer3) {
    	
			model.addAttribute("movie1", movie1);
	    	model.addAttribute("movie2", movie2);
	       	model.addAttribute("movie3", movie3);
	    	model.addAttribute("movie4", movie4);
    	
    	if (answer1.equals("3") && answer2.equals("3") && answer3.equals("2")) {
    		String response= "Vous avez survécu !";
    		model.addAttribute("response", response);
    		return "quizz2won";
    	} else {
    		return "loose2";
    	}   	       
    }
    
    @GetMapping("/fight2Choice")
    public String fight2Choice(Model model) {
    	model.addAttribute("monster1", monster4);
    	model.addAttribute("monster2", monster5);
    	model.addAttribute("monster3", monster6);
    	
    	
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
    	
    	return "fight2Choice";
    	
    }
    
    @GetMapping("/fight2/{num}")
    public String fight2(@PathVariable int num, Model model) {
      	//Reset life
    	you.setLife(100);
    	for (int i=0; i<3; i++) {
	    	fighters2[i].setLife(100);
    	}
    	
    	//Fight
    	you.setLife(100);
		ArrayList<String> fight2Infos = new ArrayList<String>();
		String linkFinal = new String();
		fight2Infos.add("Le combat a commencé !");
		while (fighters2[num -1].getLife() > 0 && you.getLife() > 0) {
			fight2Infos.add(fighters2[num -1].takeHit(you));		
			if (fighters2[num -1].getLife() > 0) {
				fight2Infos.add(you.takeHit(fighters2[num -1]));
			}
		}
		if (you.getLife() > 0) {
			fight2Infos.add("Vous avez survécu !");
			linkFinal  = "/win";
		} else {
			fight2Infos.add("Vous n'avez pas survécu...");
			linkFinal = "/loose3";
		}
		
		model.addAttribute("linkFinal", linkFinal);
		model.addAttribute("fightResult", fight2Infos);
    	model.addAttribute("monster", monsters2[num -1]);
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
    	
        return "fight2";
    }
    
    
    @GetMapping("/win")
    public String win(Model model) {
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
        return "win";
    }
    
    @GetMapping("/loose")
    public String loose(Model model) {
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
        return "loose";
    }
    
    @GetMapping("/loose1")
    public String loose1(Model model) {
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
        return "loose1";
    }
    
    
    @GetMapping("/loose2")
    public String loose2(Model model) {
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
        return "loose2";
    }
    
    @GetMapping("/loose3")
    public String loose3(Model model) {
    	model.addAttribute("movie1", movie1);
    	model.addAttribute("movie2", movie2);
    	model.addAttribute("movie3", movie3);
    	model.addAttribute("movie4", movie4);
        return "loose3";
    }
    
    
    @GetMapping("/test")
    public String test() {
        return "test";
    }
    
    
    @GetMapping("/movies")
    public String movies(Model model, @RequestParam int id) {
        model.addAttribute("movieInfos", movie(id));
        return "movies";
    }
    
    @GetMapping("/monsters")
    public String monsters(Model model, @RequestParam int id) {
        model.addAttribute("monsterInfos", monster(id));
        return "monsters";
    }
    
    
    public Movie movie(int id) { 	
        WebClient webClient = WebClient.create(HACKLOWEEN_URL);
        Mono<String> call = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movies/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);

        String response = call.block();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
        	actualObj = objectMapper.readTree(response);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        response =  actualObj.get("movie").toString();
        
        Movie movieObject = null;
        try {
        	movieObject = objectMapper.readValue(response, Movie.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }   	
    	return movieObject;
    }
    
    public Monster monster(int id) { 	
        WebClient webClient = WebClient.create(HACKLOWEEN_URL);
        Mono<String> call = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/monsters/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);

        String response = call.block();
        

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
        	actualObj = objectMapper.readTree(response);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        response =  actualObj.get("monster").toString();
        
        Monster monsterObject = null;
        try {
        	monsterObject = objectMapper.readValue(response, Monster.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return monsterObject;
    }
}