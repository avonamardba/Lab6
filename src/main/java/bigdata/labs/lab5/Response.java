package bigdata.labs.lab5;

public class Response {
    private String hostName;
    private Long responseTime;

    Response(String hostName, Long responseTime) {
        this.hostName = hostName;
        this.responseTime = responseTime;
    }

    public String getHostName() {
        return hostName;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }
}
