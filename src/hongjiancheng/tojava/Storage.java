package hongjiancheng.tojava;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private String path;
    public  Storage(String s){
        this.path=s;
    }
    private List<String> getLine() throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        File f = new File(path);
        Scanner s = new Scanner(f);
        while(s.hasNext()){
            lines.add(s.nextLine());
        }
        return lines;
    }
    private static Task createTask(String line) throws TaskManagerException {
        String type=line.split("\\|")[0];
        String isDone=line.split("\\|")[1];
        String detail=line.split("\\|")[2];
        if(type.contains("D")){
            String due=line.split("\\|")[3];
            if(isDone.contains("0")){
                return new Deadline(detail.substring(1),false,Parser.convertDeadline(due.substring(1)));
            }else if(isDone.contains("1")){
                return new Deadline(detail.substring(1),true,Parser.convertDeadline(due.substring(1)));
            }else{
                throw new TaskManagerException("Missing isDone element for CREATE TASK");
            }
        }else if(type.contains("T")){
            if(isDone.contains("0")){
                return new ToDo(detail.substring(1),false);
            }else if(isDone.contains("1")){
                return new ToDo(detail.substring(1),true);
            }
        }else{
            throw new TaskManagerException("Missing type element for CREATE TASK");
        }
        return new ToDo();
    }
    private static String outputTask(Task out){
        if(!out.print().contains("do by")){
            if(out.getDone()){
                return "T | 1 | "+out.getText()+System.lineSeparator();
            }else{
                return "T | 0 | "+out.getText()+System.lineSeparator();
            }
        }else if(out.print().contains("do by")){
            if(out.getDone()){
                return "D | 1 | "+out.getText()+" | "+((Deadline)out).getDue()+System.lineSeparator();
            }else{
                return "D | 0 | "+out.getText()+" | "+((Deadline)out).getDue()+System.lineSeparator();
            }
        }
        return System.lineSeparator();
    }
    private List<Task> getTasksFromFile() throws FileNotFoundException{
        List<Task> loadedTasks = new ArrayList<>();
        Ui a=new Ui();
        try {
            List<String> lines = getLine() ;
            for (String line : lines) {
                if (line.trim().isEmpty()) { //ignore empty lines
                    continue;
                }
                loadedTasks.add(createTask(line)); //convert the line to a task and add to the list
            }
            System.out.println("File successfully loaded");
        } catch (TaskManagerException e1) {
            a.printError(("Problem encountered while loading data: " +e1.getMessage()));
        }
        return loadedTasks;
    }
    public List<Task> load(){
        try {
            List<Task> tasks = getTasksFromFile();
            return tasks;
        }catch (FileNotFoundException e) {
            System.out.println("There is no file in the path: "+e.getMessage());
            List<Task> tasks = new ArrayList();
            return tasks;
        }
    }
    public void save(List<Task> changed){
        List<String> toAdd=new ArrayList<>();
        for(int i=0;i<changed.size();i++){
            toAdd.add(outputTask(changed.get(i)));
        }
        try {
            FileWriter fw = new FileWriter(path);
            for (int i = 0; i < toAdd.size(); i++) {
                fw.write(toAdd.get(i));
            }
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void changePath(String newPath){
        this.path=newPath;
    }
}
