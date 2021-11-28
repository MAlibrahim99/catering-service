/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package catering.catalog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogController {


	@GetMapping("/welcome")
	public String index() {
		return "/welcome";
	}




	@GetMapping("/")
	public String start() {
		return "index";
	}

	@GetMapping("/offer")
	public String offer() {
		return "offer";
	}

	@GetMapping("/eventcatering")
	public String event(Model model) {
		String picture = "/resources/img/event-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Eventcatering";
		model.addAttribute("headline", headline);
		return "catalog";
	}
	@GetMapping("/partyservice")
	public String party(Model model) {
		String picture = "/resources/img/party-detail.jpg";
		model.addAttribute("picture", picture);

		String headline = "Partyservice";
		model.addAttribute("headline", headline);
		return "catalog";
	}
	@GetMapping("/rentacook")
	public String cook(Model model) {
		String picture = "/resources/img/cook-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Rent-A-Cook";
		model.addAttribute("headline", headline);
		return "catalog";
	}
	@GetMapping("/mobilebreakfast")
	public String mobile(Model model) {
		String picture = "/resources/img/mobile-detail.jpg";
		model.addAttribute("picture", picture);
		String headline = "Mobile Breakfast";
		model.addAttribute("headline", headline);
		return "catalog";
	}

}
