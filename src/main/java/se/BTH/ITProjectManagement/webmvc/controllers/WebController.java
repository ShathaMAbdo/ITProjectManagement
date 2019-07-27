package se.BTH.ITProjectManagement.webmvc.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class WebController {
/*
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;
	
	@RequestMapping("/")
	public ModelAndView welcome() {
		return new ModelAndView("jsp/home", "command", new Booking());
	}
	
	@RequestMapping("/bookings")
	public ModelAndView getBooking() {
		
		Sprint[] sprints = restTemplate.getForObject("http://Sprintmanag-SERVICE/list", Booking[].class);
		
		ModelAndView model = new ModelAndView("jsp/list");
		model.addObject("bookings", Arrays.asList(booking));
		return model;
				
	}
	
	@RequestMapping(path="/add", method=RequestMethod.POST)
	public ModelAndView addBooking(@ModelAttribute Booking booking) {
	
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, Object> variables = buildRequest(booking);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(variables, requestHeaders);
		
		ResponseEntity<?> response = restTemplate.postForEntity("http://BOOKING-SERVICE/add", requestEntity, ResponseEntity.class);
		//ResponseEntity<?> response = restTemplate.exchange("http://BOOKING-SERVICE/add", HttpMethod.POST, requestEntity, ResponseEntity.class);
		
		System.out.println(response);
		
		return welcome();
				
	}
	
	
	protected MultiValueMap<String, Object> buildRequest(Booking booking)
	{
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("code", booking.getCode());
		param.add("FlightNumber", booking.getFlightNumber());
		param.add("Name", booking.getName());
		param.add("Surname", booking.getSurname());
		param.add("Seat", booking.getSeat());
		return param;
	}
*/
}
