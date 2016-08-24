package com.vmware.samples.bosphorus.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.vmware.samples.bosphorus.models.MachineConfiguration;
import com.vmware.samples.bosphorus.models.MachineConfigurationBundle;
import com.vmware.samples.bosphorus.models.MachineTemplate;
import com.vmware.samples.bosphorus.models.MachineTemplate.FieldConfig;
import com.vmware.samples.bosphorus.vra.JSONHelper;

@Controller
public class RequestMachineController extends AbstractController {	
	Log log = LogFactory.getLog(RequestMachineController.class);

	@RequestMapping(value="/editrequest/{blueprintId}", method=RequestMethod.GET)
	public String editRequest(@PathVariable String blueprintId, Model model) throws ClientProtocolException, IOException, HttpException {
		// We could send the raw response to the UI and let the HTML-template sort it out, but this
		// structure is pretty complex and would give rise to some... interesting Thymeleaf code. So 
		// we're being nice and reducing it into something a bit easier to handle.
		// If you absolutely feel a need to use the raw data, use the passThroughGet call!
		//
		Map<String, Object> data = this.getVra().get("/catalog-service/api/consumer/entitledCatalogItems/" + blueprintId + "/requests/schema");
		ArrayList<MachineTemplate> result = new ArrayList<MachineTemplate>();
		Collection<Map<String, Object>> machines = JSONHelper.getListOfComplex(data, "fields");
		for(Map<String, Object> machine : machines) {
			String name = (String) machine.get("label");
			Collection<Map<String, Object>> fields = JSONHelper.getListOfComplex(machine, "dataType.schema.fields");
			if(fields == null)
				continue;
			MachineTemplate.FieldConfig cpu = null, ram = null, storage = null;
			for(Map<String, Object> field : fields) {
				String id = (String) field.get("id");
				Collection<Map<String, Object>> facets = JSONHelper.getListOfComplex(field, "state.facets");
				if(facets == null)
					continue;
				if("memory".equals(id)) 
					ram = FieldConfig.fromFacets(facets);
				else if("cpu".equals(id)) 
					cpu = FieldConfig.fromFacets(facets);
				else if("storage".equals(id)) 
					storage = FieldConfig.fromFacets(facets);
			}
				
			// If we came across something that doesn't even have a CPU, it's probably not a machine
			// definition, so skip it. May do something nicer here at some point!
			//
			if(cpu != null)
				result.add(new MachineTemplate(name, cpu, ram, storage));
		}
		model.addAttribute("templates", result);
		model.addAttribute("blueprint", this.getVra().get("/catalog-service/api/consumer/entitledCatalogItems/" + blueprintId).get("catalogItem"));
		model.addAttribute("machines", new MachineConfigurationBundle(blueprintId, result));
		return "editrequest";
	}
	
	@RequestMapping(value="/submitrequest", method=RequestMethod.POST)
	public View submitRequest(HttpServletRequest request, @ModelAttribute("machines") MachineConfigurationBundle machines) throws ClientProtocolException, IOException, HttpException {
		Map<String, Object> template = this.getVra().get("/catalog-service/api/consumer/entitledCatalogItems/" + machines.getBlueprintId() + "/requests/template");
		Map<String, Object> templateData = JSONHelper.getComplex(template, "data");
		for(Map.Entry<String, MachineConfiguration> machineEntry : machines.getMachines().entrySet()) {
			MachineConfiguration machine = machineEntry.getValue();
			Map<String, Object> subTemplate = JSONHelper.getComplex(templateData, machineEntry.getKey() + ".data");
			subTemplate.put("cpu", machine.getNumCPUs());
			subTemplate.put("memory", machine.getMemoryMB());
		}
		this.getVra().post("/catalog-service/api/consumer/entitledCatalogItems/" + machines.getBlueprintId() + "/requests", template);
		return new RedirectView("/home");
	}
}
