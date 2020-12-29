package com.shindo.kill.server.controller;

/**
 * @Description: 基础控制器
 * @Author: 杨耿
 * @Date: Create in 2020/12/29
 */
@Controller
public class BaseController {
	@RequestMappering(value = "/welcome")
	public String welcome() {
		System.out.println("come in welcome page!");
		return "welcome";
	}
	

}
