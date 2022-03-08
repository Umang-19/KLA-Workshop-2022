import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyMain {
    private static Logger logger = Logger.getLogger("MyLog");
    private static FileHandler fh;

    // Time Function
    public static void TimeFunction(String FunctionInput, long time) throws InterruptedException {
        // log entry would go here
        logger.info(FunctionInput);
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
        logger.info(root + " " + " Entry");

        Map<?, ?> activitiesMap = (Map<?, ?>) data.get(root);
//        System.out.println(activitiesMap.get("Activities"));

        Map<?, ?> allTaskMap = (Map<?, ?>) activitiesMap.get("Activities");

        // Iterating over all activities
        for (Object name : allTaskMap.keySet()) {
            String taskname = (String) name;
            logger.info(taskname + " " + " Entry");

            if (taskname.startsWith("Task")) {
                Map<?, ?> alltasks = (Map<?, ?>) allTaskMap.get(taskname);
                Map<?, ?> mytask = (Map<?, ?>) alltasks.get("Inputs");

                String FunctionInput = (String) mytask.get("FunctionInput");
                String exectime = (String) mytask.get("ExecutionTime");
                long time = Long.parseLong(exectime);
//                System.out.println(FunctionInput + " " + time);

                String str = root + "." + taskname + "." + "Executing " + FunctionInput + "(" + exectime + ")";
                TimeFunction(str, time);

            } else {

                Map<?, ?> activitiesMap2 = (Map<?, ?>) allTaskMap.get(taskname);
                Map<?, ?> allTaskMap2 = (Map<?, ?>) activitiesMap2.get("Activities");

                for (Object name2 : allTaskMap2.keySet()) {
                    String taskname2 = (String) name2;
//                    System.out.println("Task 2 = " + taskname2);
                    logger.info(taskname2 + " " + " Entry");
                    Map<?, ?> alltasks2 = (Map<?, ?>) allTaskMap2.get(taskname2);
                    Map<?, ?> mytask2 = (Map<?, ?>) alltasks2.get("Inputs");

                    String FunctionInput = (String) mytask2.get("FunctionInput");
                    String exectime = (String) mytask2.get("ExecutionTime");
                    long time = Long.parseLong(exectime);
                    System.out.println(FunctionInput + " " + time);

                    String str = root + "." + taskname + "." + taskname2 + "." + "Executing " + FunctionInput + "(" + exectime + ")";
                    TimeFunction(str, time);
                    logger.info(taskname2 + " " + " Exit");
                }
                logger.info(taskname + " " + " Exit");
            }
        }
    }
}


