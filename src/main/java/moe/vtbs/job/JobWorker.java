package moe.vtbs.job;

import moe.vtbs.DDatJava;

@Deprecated
public class JobWorker implements Runnable{
    @Override
    public void run() {
        DDatJava.LOGGER.debug("Requesting for a job...");

        if(!DDatJava.JOB_WS_CLIENT.isClosed()){
            DDatJava.JOB_WS_CLIENT.send("DDhttp");
        }else{
            DDatJava.JOB_WS_CLIENT.connect();
            DDatJava.LOGGER.error("Websocket Closed!!! Attempted to open, waiting for next round");
        }
    }
}
