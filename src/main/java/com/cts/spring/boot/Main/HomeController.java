package com.cts.spring.boot.Main;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class HomeController {
	
	public final static String AUTH_KEY_FCM = "AAAArjHd0i0:APA91bEPAF02plVTrULYG7vLRpAAtdvbL7aMxMlPBfpqivfSKhqlIIfUmPO5gGxEKskLp3_XRiYF12nJWLYKDLpcPzOzKKYt_hPFWC2-k8-udJHqxOOqYlEKcKVMCj4klww9_hDQaRdhIpi67SyDtszmLSY5xzRT5Q";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	@Autowired
	PersonRepo personRepo;
	
	@RequestMapping(value="/persons",method=RequestMethod.GET)
	public List<Person> getPersons(){
		List<Person> personList = new ArrayList<>();
		personList.add(new Person("Bala", 27));
		personList.add(new Person("Guna", 29));
		personList.add(new Person("Subu", 25));
		System.out.println("called get person api .... ");
		return personList;
	}
	
	@RequestMapping(value="/ldap",method=RequestMethod.GET)
	public List<LdapPerson> getLdapPersons(){
		System.out.println("called get ldap person api .... ");
		return personRepo.getAllPerson();
	}
	
	@RequestMapping(value="/androidpush/{userDeviceIdKey}", method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pushNotification(@PathVariable String userDeviceIdKey) throws URISyntaxException{
		
		System.out.println(userDeviceIdKey);
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "key="+AUTH_KEY_FCM);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("to", userDeviceIdKey);
		
		/*JSONObject info = new JSONObject();
		info.put("title", "Notificatoin Title");   // Notification title
		info.put("body", "Hello Test notification"); // Notification body
*/		
		map.add("notification","hi");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		System.out.println(request.hasBody());
		ResponseEntity<String> response = restTemplate.postForEntity(new URI(API_URL_FCM), request, String.class);
		//restTemplate.postForObject(API_URL_FCM, request, String.class, uriVariables)
		//String response = restTemplate.exchange(API_URL_FCM, HttpMethod.POST, request, String.class);
		System.out.println(response.getBody());
		
		return response;
	}
}
