package org.imis.generator;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import org.imis.model.CharacteristicSet;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;

public class QueryGenerator {
    public static HashMap<String, Integer> propertiesSet = new HashMap<String, Integer>();
    public static HashMap<Integer, String> reversePropertiesSet = new HashMap<Integer, String>();

    public void start(int evoVersions){
        File[] files = new File(System.getProperty("user.dir")).listFiles();
        Model model = ModelFactory.createDefaultModel();
        //model.set
        for(File file : files){
            if(!file.getName().contains("owl")) continue;
            try{

                FileInputStream in = new FileInputStream(file);
                model.read(in, "http://example.com", "RDF/XML");
                in.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        long previousSize = model.size();
        System.out.println("v0 size: " + previousSize);

        String queryString = " SELECT ?s ?p WHERE {?s ?p ?o} GROUP BY ?s ?p ORDER BY ?s ";

        Query query = QueryFactory.create(queryString);

        QueryExecution qexec = QueryExecutionFactory.create(query, model);

        ResultSet rs = qexec.execSelect();

        Resource previous = null;

        HashMap<Node, CharacteristicSet> characteristicSetMap = new HashMap<Node, CharacteristicSet>();

        HashSet<Resource> properties = new HashSet<Resource>();

        CharacteristicSet cs ;

        int nextInd = 0;

        int propIndex = 0;

        HashSet<Integer> objects = new HashSet<Integer>();

        while(rs.hasNext()){

            QuerySolution sol = rs.next();

            Resource subject = sol.getResource("s");

            Resource predicate = sol.getResource("p");

            if(!propertiesSet.containsKey(predicate.getURI())){
                reversePropertiesSet.put(propIndex, predicate.getURI());
                propertiesSet.put(predicate.getURI(), propIndex++);

            }

            if(!subject.equals(previous) && previous != null){

                cs = new CharacteristicSet(properties);

                //uniqueCharacteristicSets.add(cs);

                characteristicSetMap.put(previous.asNode(), cs);

                properties = new HashSet<Resource>();


            }

            if(!properties.contains(predicate))
                properties.add(predicate);

            previous = subject;
        }
        qexec.close();
        //don't forget the last one
        cs = new CharacteristicSet(properties);
        //uniqueCharacteristicSets.add(cs);
        characteristicSetMap.put(previous.asNode(), cs);

        //System.out.println("Unique CS: " + uniqueCharacteristicSets.size());
        System.out.println("Unique nodes with CS: " + characteristicSetMap.size());
        HashSet<CharacteristicSet> ucs = new HashSet<CharacteristicSet>();
        for(Node n : characteristicSetMap.keySet()){
            ucs.add(characteristicSetMap.get(n));
        }
        System.out.println("Unique CS: " + ucs.size());
        System.out.println("Starting model size: " + model.size());
        long startSize = model.size();

        WorkloadGenerator workload = new WorkloadGenerator(evoVersions-1);
        workload.generateWorkload();
        int i = 0;
        while(true){
            //if(true) break;
            try{
                files = new File(System.getProperty("user.dir")+"/v"+i).listFiles();
                Model modelin = ModelFactory.createDefaultModel();
                for(File file : files){
                    if(!file.getName().contains(".owl")) continue;{
                        FileInputStream in = new FileInputStream(file);
                        modelin.read(in, "http://example.com", "RDF/XML");
                        in.close();
                    }
                }
                //System.out.println("v"+(int)(i+1)+ " size: " + (previousSize+modelin.size()));
                double shift = (double)modelin.size()/(double)previousSize;
                System.out.println(shift);
                System.out.println(previousSize);
                previousSize += modelin.size();
                model.add(modelin);
                System.out.println("model size:" + model.size());
                modelin.close();
                i++;
            }
            catch(Exception e){
                break;
            }
        }
        System.out.println("Achieved change: " + (double)(model.size()-startSize)/model.size());
        model.close();
    }
}
