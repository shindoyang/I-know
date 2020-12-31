package com.shindo.kill.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 基础控制器
 * @Author: 杨耿
 * @Date: Create in 2020/12/29
 */
@Controller
public class BaseController {
	@RequestMapping(value = "/welcome")
	public String welcome() {
		System.out.println("come in welcome page!");
		return "welcome";
	}


}
