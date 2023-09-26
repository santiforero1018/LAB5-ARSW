/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    private final Map<Tuple<String, String>, Blueprint> blueprints = new HashMap<>();

    public InMemoryBlueprintPersistence() {

        Blueprint bp1 = new Blueprint("john", "thepaint", new Point[] { new Point(0, 0), new Point(10, 10) });

        Blueprint bp2 = new Blueprint("Martha", "thepaint1", new Point[] { new Point(10, 15), new Point(20, 20) });

        Blueprint bp3 = new Blueprint("john", "thepaint2", new Point[] { new Point(16, 7), new Point(30, 30) });

        try {
            this.saveBlueprint(bp1);
            this.saveBlueprint(bp2);
            this.saveBlueprint(bp3);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        Set<Blueprint> resp = new HashSet<>();

        for (Map.Entry<Tuple<String, String>, Blueprint> space : blueprints.entrySet()) {
            resp.add(space.getValue());
        }
        if (resp.isEmpty()) {
            throw new BlueprintNotFoundException("Error 404 NOT FOUND: trying to find blueprints");
        }
        return resp;
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        } else if(bp.getAuthor().isEmpty() || bp.getName().isEmpty()){
            throw new BlueprintPersistenceException("The given blueprint hasn't author or name");
        } else {
            blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {

        Blueprint resp = blueprints.get(new Tuple<>(author, bprintname));

        if (resp == null) {
            throw new BlueprintNotFoundException("Error 404 NOT FOUND: trying to find blueprints");
        }

        return resp;
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> resp = new HashSet<>();

        for (Map.Entry<Tuple<String, String>, Blueprint> space : blueprints.entrySet()) {
            if (space.getKey().getElem1().equals(author)) {
                resp.add(space.getValue());
            }
        }
        if (resp.isEmpty()) {
            throw new BlueprintNotFoundException("Error 404 NOT FOUND: trying to find blueprints");
        }
        return resp;

    }

    @Override
    public void updateBluePrint(Blueprint obp,String author, String name) throws BlueprintPersistenceException {
        try {
            Point[] aux = (Point[]) blueprints.get(new Tuple<>(obp.getAuthor(), obp.getName())).getPoints().toArray();
            blueprints.remove(new Tuple<>(obp.getAuthor(), obp.getName()));
            this.saveBlueprint(new Blueprint(author, name, aux));
            
        } catch (BlueprintPersistenceException e) {
            throw new BlueprintPersistenceException(e.getMessage());
        }

    }
}
