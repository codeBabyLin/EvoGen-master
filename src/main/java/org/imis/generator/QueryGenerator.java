package org.imis.generator;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import org.imis.model.CharacteristicSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;

public class QueryGenerator {
    //public static HashMap<String, Integer> propertiesSet = new HashMap<String, Integer>();
    //public static HashMap<Integer, String> reversePropertiesSet = new HashMap<Integer, String>();

    public void start(int evoVersions){
        String dirPath = System.getProperty("user.dir") + "\\Csv\\";
        try {
            PrintStream queryPrint = new PrintStream(new FileOutputStream(new File(dirPath, "Query.txt")));
            if(evoVersions <= 1){
                String q1 = String.format("match(n) return n ");
                String q2 = String.format("match(n) return count(n) ");
               queryPrint.println(q1);
               queryPrint.println(q2);

            }
            else {
                for (int i = 0; i < evoVersions; i++) {
                    String q1 = String.format("match(n) at %d return n ",i);
                    String q2 = String.format("match(n) at %d return count(n) ",i);
                    queryPrint.println(q1);
                    queryPrint.println(q2);
                }
                for(int i = 0;i<evoVersions -1;i++){
                    String q1 = String.format("match(n) after %d return count(n) ",i);
                    queryPrint.println(q1);
                }
                for(int i =0;i<evoVersions-1;i++){
                    String q1 = String.format("match(n) at %d optional match(n) at %d ",i,i+1);
                    queryPrint.println(q1);
                }
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
