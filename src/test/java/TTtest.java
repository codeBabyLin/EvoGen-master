import org.imis.generator.instance.StudentInstance;

import java.util.ArrayList;

public class TTtest {

    public static void change(ArrayList<StudentInstance> studentd){
        StudentInstance si = studentd.get(0);
        si.setId(1999);
    }
    public static void main(String []args){

        ArrayList<StudentInstance> studentd = new ArrayList<>();
        StudentInstance si = new StudentInstance();
        si.setId(1);
        studentd.add(si);
        si = new StudentInstance();
        si.setId(2);
        studentd.add(si);

        si = new StudentInstance();
        si.setId(3);
        studentd.add(si);
        change(studentd);
        for(int i = 0;i<studentd.size();i++){
            System.out.println("id==="+studentd.get(i).getId());
        }


    }
}
