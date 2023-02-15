package org.imis.generator.instance;

import java.util.ArrayList;

public class PublicationInstance {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getPublicationAuthor() {
        return publicationAuthor;
    }

    public void setPublicationAuthor(ArrayList<Integer> publicationAuthor) {
        this.publicationAuthor = publicationAuthor;
    }
    public void addPublicationAuthor(int id){
        this.publicationAuthor.add(id);
    }


    int id;
    String name;
    ArrayList<Integer> publicationAuthor = new ArrayList<>();
}
