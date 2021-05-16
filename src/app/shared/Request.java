package app.shared;


enum RequestType {
    REQUEST_JOB,
    SEND_VALUE,
}

public class Request {
    private final RequestType requestType;
    private String data;

    public Request(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
