package app.shared;

enum ResponseType{
    SEND_JOB,
    ACK,
    STOP
}

public class Response {

    private final ResponseType responseType;
    private String data;

    public Response(ResponseType requestType) {
        this.responseType = requestType;
    }

    public ResponseType getRequestType() {
        return responseType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
