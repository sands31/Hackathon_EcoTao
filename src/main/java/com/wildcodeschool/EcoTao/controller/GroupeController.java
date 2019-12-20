package com.wildcodeschool.EcoTao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupeController {
	@GetMapping("/groupe")
	public String groupe(Model model) {
		return "groupe";
	}
	
}
