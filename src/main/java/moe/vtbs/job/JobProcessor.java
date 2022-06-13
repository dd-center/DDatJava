package moe.vtbs.job;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import moe.vtbs.DDatJava;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Deprecated
public class JobProcessor implements Runnable {

    private static final HttpTransport transport = new NetHttpTransport();
    private final JobRequest jobInfo;

    public JobProcessor(String jobJson){
        Gson gson = new Gson();
        jobInfo = gson.fromJson(jobJson, JobRequest.class);
    }

    public String requestJob(){
        GenericUrl url = new GenericUrl(jobInfo.getUrl());
        try {
            HttpRequest request = transport.createRequestFactory().buildGetRequest(url);
            HttpHeaders headers = request.getHeaders();
            headers.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.2 Safari/605.1.15");
            HttpResponse response = request.execute();
            return IOUtils.toString(response.getContent(), StandardCharsets.UTF_8);
        }catch (IOException exception){
            DDatJava.LOGGER.error("Errored while requesting!");
            DDatJava.LOGGER.error(exception.getMessage());
            return null;
        }
    }

    @Override
    public void run() {
        if(this.jobInfo.isValidJob()) {
            DDatJava.LOGGER.info("Job coming in! Key:" + this.jobInfo.getKey());
            String jobRequestContent = this.requestJob();
            String jobResponseStr;

            if(jobRequestContent == null){
                jobResponseStr = "{\"key\":\"" + this.jobInfo.getKey() + "\", \"data\": \"ERROR!\" }";
            }else{
                Gson gson = new Gson();
                jobResponseStr = gson.toJson(new JobResponse(this.jobInfo.getKey(), jobRequestContent));
            }
            DDatJava.JOB_WS_CLIENT.send(jobResponseStr);
            DDatJava.LOGGER.info("Request Done for key " + this.jobInfo.getKey());
        }else{
            DDatJava.LOGGER.warn("An unsupported request type has came in, job dropped! Key: " + this.jobInfo.getKey());
        }
    }

}
