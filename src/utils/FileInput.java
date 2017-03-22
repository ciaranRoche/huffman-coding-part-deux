package utils;

import edu.princeton.cs.introcs.In;
import models.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ciaranroche on 22/03/2017.
 */
public class FileInput {
    public List<Data> loadData(String filename) throws Exception{
        File file = new File(filename);
        In in = new In(file);
        String d = "";
        List<Data> data = new ArrayList<>();
        while(!in.isEmpty()){
            String details = in.readLine();
            d = d + details;
            data.add(new Data(d));
        }
        return data;
    }
}
