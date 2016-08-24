package com.vmware.samples.bosphorus.controllers;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController()
@RequestMapping("/rest")
public class RESTController extends AbstractController {
	
	@RequestMapping(value = "/passthrough/**", method=RequestMethod.GET, produces="application/json")
    public String getPassthrough(final HttpServletRequest request) throws JsonMappingException, ClientProtocolException, IOException, HttpException {

        String path = (String) request.getAttribute(
            HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        AntPathMatcher apm = new AntPathMatcher();
        String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);        
        return this.getVra().getString('/' + finalPath);
    }
	
	@RequestMapping(value = "/passthrough/**", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public String postPassthrough(final HttpServletRequest request) throws JsonMappingException, ClientProtocolException, IOException, HttpException {
        String path = (String) request.getAttribute(
            HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        AntPathMatcher apm = new AntPathMatcher();
        String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);        
        return new ObjectMapper().writeValueAsString(this.getVra().postReturnString('/' + finalPath, IOUtils.toString(request.getInputStream(), Charset.defaultCharset())));
    }
}
