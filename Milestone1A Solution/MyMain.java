import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyMain {

    static{
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$ts%5$s%6$s%n");
    }

    private static Logger logger = Logger.getLogger("MyLog");
    private static FileHandler fh;
    //private static String input = "2014-11-10 04:05:06.999999".replace( " " , "T" );

    // Time Function
    public static void TimeFunction(String FunctionInput, long time) throws InterruptedException {
        // log entry would go here
        //logger.info(LocalDateTime.now() + input);
        logger.info(";" + FunctionInput);
        Thread.sleep(time * 1000);
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        // Log File generator code
        try {
            fh = new FileHandler("./src/main/resources/Milestone1A.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Parsing YAML File
        InputStream inputstream = new FileInputStream(new File("./src/main/resources/Milestone1A.yaml"));
        Yaml yaml = new Yaml();
        Map<?, ?> data = yaml.loadAs(inputstream, Map.class);

        Map.Entry<?, ?> entry = data.entrySet().iterator().next();
        String root = (String) entry.getKey();
        logger.info(";" + root + " Entry");

        Map<?, ?> activitiesMap = (Map<?, ?>) data.get(root);
        Map<?, ?> allTaskMap = (Map<?, ?>) activitiesMap.get("Activities");

        // Iterating over all activities
        for (Object name : allTaskMap.keySet()) {
            String taskname = (String) name;

            if (taskname.startsWith("Task")) {
                processTask(root, allTaskMap, taskname, root);
            } else {
                processFlow(allTaskMap, taskname, root);
            }
        }

        logger.info(";" + root + " Exit");
    }

    // Process Task
    private static void processTask(String root, Map<?, ?> allTaskMap, String taskname, String parent) throws InterruptedException {
        logger.info(";" + parent + "." + taskname +  " Entry");
        Map<?, ?> alltasks = (Map<?, ?>) allTaskMap.get(taskname);
        Map<?, ?> mytask = (Map<?, ?>) alltasks.get("Inputs");

        String FunctionInput = (String) mytask.get("FunctionInput");
        String exectime = (String) mytask.get("ExecutionTime");
        long time = Long.parseLong(exectime);

        String str = parent + "." + taskname + " Executing TimeFunction"  + "(" + FunctionInput + ", " + exectime + ")";
        TimeFunction(str, time);
        logger.info(";" + parent + "." + taskname +  " Exit");
    }

    // Process Flow
    private static void processFlow(Map<?, ?> allTaskMap, String taskname, String parent) throws InterruptedException {
        Map<?, ?> activitiesMap2 = (Map<?, ?>) allTaskMap.get(taskname);
        Map<?, ?> allTaskMap2 = (Map<?, ?>) activitiesMap2.get("Activities");

        for (Object name2 : allTaskMap2.keySet()) {
            String taskname2 = (String) name2;

            if (taskname2.startsWith("Task")) {
                processTask(taskname, allTaskMap2, taskname2, parent + "." + taskname);
            } else {
                processFlow(allTaskMap2, taskname2, parent + "." + taskname);
            }
        }
        logger.info(";" + parent + "." + taskname + " Exit");
    }
}


