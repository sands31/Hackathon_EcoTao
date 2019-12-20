package com.wildcodeschool.EcoTao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LeaderBoardController {
	
	@GetMapping("/leaderboard")
	public String leaderboard() {
		return "leaderboard.html";
	}
}
