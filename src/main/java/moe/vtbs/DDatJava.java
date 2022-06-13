package moe.vtbs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import moe.vtbs.job.JobSocket;
import moe.vtbs.job.JobWorker;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Deprecated
public class DDatJava {

    //public static final String VERSION = "1.0.0";
    @Deprecated
    public static final String VERSION = DDCore.getVersion();
    //public static final Logger LOGGER = LoggerFactory.getLogger(DDatJava.class);
    @Deprecated
    public static final Logger LOGGER = DDCore.getLogger();
    public static Config APP_CONFIG = ConfigFactory.defaultApplication();

    public static WebSocketClient JOB_WS_CLIENT;

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // Load Config
        if (args.length == 1) {
            LOGGER.info("Passed In Config file path:" + args[0]);
            APP_CONFIG = ConfigFactory.parseFile(new File(args[0])).resolve();
        } else {
            LOGGER.info("Using default config!");
            APP_CONFIG = ConfigFactory.load("app.conf");
        }
        LOGGER.debug("Socket URL: " + getWsUrl());
        // connect ws
        JOB_WS_CLIENT = new JobSocket(URI.create(getWsUrl()));
        LOGGER.info("Connecting to socket...");
        JOB_WS_CLIENT.connect();
        // waiting socket to be opened
        while (!JOB_WS_CLIENT.isOpen()) ;
        LOGGER.info("Connected to socket!");
        // Run Thread
        executorService.scheduleAtFixedRate(new JobWorker(),
                0, DDatJava.APP_CONFIG.getLong("app.interval"),
                TimeUnit.SECONDS);
    }

    public static String getWsUrl() {
        // JDK8似乎不支持 URLEncoder.encode(String, Charset)
        try {
            return "wss://cluster.vtbs.moe/?runtime=java&version=" + VERSION + '_' + System.getProperty("java.version")
                    + "&platform=" + URLEncoder.encode(System.getProperty("os.name") + '-' + System.getProperty("os.version"), "UTF-8")
                    + '_' + System.getProperty("os.arch") + "&name="
                    + URLEncoder.encode(APP_CONFIG.getString("app.nickname"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
