package models;

/**
 * Created by ciaranroche on 22/03/2017.
 */
public class Data {
    private String data;

    public Data(String data){
        this.data = data;
    }

    public String toString(){
        return data;
    }

    public void addData(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }

}
