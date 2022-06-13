package moe.vtbs.job;

import java.io.Serializable;

@Deprecated
public class JobResponse implements Serializable {

    public JobResponse(String key, String data) {
        this.key = key;
        this.data = data;
    }

    private String key;
    private String data;
}
