package com.vtbs;

import java.io.Serializable;

public class JobRequest implements Serializable {
    private String key;
    private JobData data;

    public String getUrl() {
        return data.getUrl();
    }

    public String getKey() {
        return key;
    }

    public boolean isValidJob(){
        return data.getType().equals("http");
    }
}

class JobData implements Serializable {
    private String type;
    private String url;

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }
}


