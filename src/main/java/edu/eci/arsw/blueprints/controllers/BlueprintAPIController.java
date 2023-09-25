/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value="/blueprints")
public class BlueprintAPIController {
    
    @Autowired
    BlueprintsServices bps;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllBlueprints(){
        try {
            return new ResponseEntity<Set<Blueprint>>(this.bps.getAllBlueprints(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<String>("Error 404 NOT FOUND: trying to find blueprints", HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value="/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> getByAuthor(@PathVariable String author){
        try {
            Set<Blueprint> data = bps.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("Error 404 NOT FOUND: trying to find blueprints by author ---> " + author, HttpStatus.NOT_FOUND);

        }
    }
    
    @RequestMapping(value="/{author}/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprint(@PathVariable String author, @PathVariable String name){
        try {
            Blueprint data = bps.getBlueprint(author,name);
            return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            return new ResponseEntity<>("Error 404 NOT FOUND: trying to find blueprints by author ---> " + author + " and name ---> " + name, HttpStatus.NOT_FOUND);

        }
    }
}

