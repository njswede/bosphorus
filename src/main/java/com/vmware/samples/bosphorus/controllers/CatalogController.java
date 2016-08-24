package com.vmware.samples.bosphorus.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vmware.samples.bosphorus.models.ActionIdentifier;
import com.vmware.samples.bosphorus.vra.OrderBy;

@Controller
public class CatalogController extends AbstractController {
	private static Log log = LogFactory.getLog(CatalogController.class);
	
	@RequestMapping(value="/catalog", method=RequestMethod.GET) 
	public String getEntitledCatalogItems(@RequestParam(required=false) String pattern, @RequestParam int start, @RequestParam int limit, Model model) throws IOException, HttpException {
		model.addAttribute("catalog", this.getVra().getPaged("/catalog-service/api/consumer/entitledCatalogItems", pattern, start, limit, null));
		model.addAttribute("pageNumber", start);
		model.addAttribute("pageSize", limit);
		return "catalog";
	}
	
	@RequestMapping(value="/requests", method=RequestMethod.GET) 
	public String getRequests(@RequestParam int start, @RequestParam int limit, Model model) throws ClientProtocolException, IOException, HttpException {
		model.addAttribute("requests", this.getVra().getPaged("/catalog-service/api/consumer/requests", null, start, limit, new OrderBy("requestNumber", OrderBy.Order.DOWN)));
		model.addAttribute("pageNumber", start);
		model.addAttribute("pageSize", limit);
		return "requests";
	}
	
	@RequestMapping(value="/resources", method=RequestMethod.GET) 
	public String getResources(@RequestParam int start, @RequestParam int limit, Model model) throws ClientProtocolException, IOException, HttpException {
		model.addAttribute("resources", this.getVra().getPaged("/catalog-service/api/consumer/resources/types/composition.resource.type.deployment", null, start, limit, new OrderBy("dateCreated", OrderBy.Order.DOWN)));
		model.addAttribute("pageNumber", start);
		model.addAttribute("pageSize", limit);
		return "resources";
	}
	
	@RequestMapping(value="/requestdetails/{id}", method=RequestMethod.GET) 
	public String getRequests(@PathVariable String id, Model model) throws ClientProtocolException, IOException, HttpException {
		model.addAttribute("request", this.getVra().get("/catalog-service/api/consumer/requests/" + id));
		return "requestdetails";
	}
	
	@RequestMapping(value="/resourcedetails/{id}", method=RequestMethod.GET) 
	public String getResourceDetails(@PathVariable String id, Model model) throws ClientProtocolException, IOException, HttpException {
		model.addAttribute("resource", this.getVra().get("/catalog-service/api/consumer/resources/" + id));
		model.addAttribute("actions", this.getVra().get("/catalog-service/api/consumer/resources/" + id + "/actions"));
		return "resourcedetails";
	}
	
	@RequestMapping(value="/submitaction", method=RequestMethod.POST) 
	public String submitAction(@RequestBody ActionIdentifier action) throws ClientProtocolException, IOException, HttpException {
		Map<String, Object> template = this.getVra().get("/catalog-service/api/consumer/resources/" + action.getResourceId() + "/actions/" + action.getActionId() + "/requests/template");
		this.getVra().post("/catalog-service/api/consumer/resources/" + action.getResourceId() + "/actions/" + action.getActionId() + "/requests", template);
		log.debug("submitaction: " + action);
		return "home";
	}
	
	@ResponseBody
	@RequestMapping(value="/catalogicon/{iconId}", method=RequestMethod.GET, produces=MediaType.IMAGE_PNG_VALUE)
	public byte[] loadVraIcon(@PathVariable String iconId) throws IOException, UnsupportedOperationException, HttpException {
		try {
			//return this.getVra().loadIconData(iconId);
			return this.getVra().getBinary("/catalog-service/api/icons/" + iconId + "/download", "image/png");
		}  catch(HttpException e) {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("static/images/" + iconId + ".png");
			if(is == null)
				throw e;
			try {
				return IOUtils.toByteArray(is);
			} finally {
				is.close();
			}
		}
	}
}
