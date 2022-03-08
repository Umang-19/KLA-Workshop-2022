import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyMain {
    private static Logger logger = Logger.getLogger("MyLog");
    private static FileHandler fh;

    public static void TimeFunction(String FunctionInput, long time) throws InterruptedException {
        // log entry would go here
        logger.info(FunctionInput);
        Thread.sleep(time*1000);
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {



        // Parsing YAML File
        InputStream inputstream = new FileInputStream(new File("./src/main/resources/Milestone1A.yaml"));
        Yaml yaml = new Yaml();
        Map<?, ?> data = yaml.loadAs(inputstream, Map.class);
        //System.out.println(data);

        // This block configure the logger with handler and formatter
        try {
            fh = new FileHandler("./src/main/resources/Milestone1A.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
//            logger.info("My first log");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map.Entry<?, ?> entry = data.entrySet().iterator().next();
        String root = (String) entry.getKey();
        logger.info(root + " " + " Entry");

        //System.out.println(data.get(root));

        Map<?, ?> activitiesMap = (Map<?, ?>) data.get(root);
        System.out.println(activitiesMap.get("Activities"));

        Map<?, ?> allTaskMap = (Map<?, ?>) activitiesMap.get("Activities");
        for (Object name : allTaskMap.keySet()) {
            String taskname = (String) name;

            logger.info(taskname + " " + " Entry");
            if(taskname.startsWith("Task"))
            {
                System.out.println(allTaskMap.get(taskname));
                Map<?, ?> alltasks = (Map<?, ?>)allTaskMap.get(taskname);
                System.out.println(alltasks.get("Inputs"));

                Map<?, ?> mytask = (Map<?, ?>)alltasks.get("Inputs");

                String FunctionInput = (String) mytask.get("FunctionInput");
                String exectime = (String) mytask.get("ExecutionTime");
                long time = Long.parseLong(exectime);
                System.out.println(FunctionInput + " " + time);

                String str = root + "." + taskname + "." + "Executing " + FunctionInput + "(" + exectime + ")" ;
                TimeFunction(str, time);

            } else {

            }

            logger.info(taskname + " " + " Exit");
        }
    }
}


