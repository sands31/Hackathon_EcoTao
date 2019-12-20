package com.wildcodeschool.EcoTao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderBoardController {
	
	@GetMapping("/leaderboard")
	public String leaderboard(Model model) {
		return "leaderboard";
	}
}
