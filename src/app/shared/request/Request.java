package app.shared.request;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Request {
    private RequestType requestType;
    private String data = "";

    public Request(){
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

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void send(PrintWriter out){
        String safeData = "req";
        if (this.data.length() > 0){
            safeData = data;
        }
        String stringBuilder = URLEncoder.encode(this.requestType.toString(), StandardCharsets.UTF_8) +
                "." +
                URLEncoder.encode(safeData, StandardCharsets.UTF_8);
        out.println(stringBuilder);
    }

    public static Request receive(BufferedReader in) throws IOException {
        String message = in.readLine();
        String[] messageParts = message.split("\\.");
        RequestType requestType = RequestType.valueOf(URLDecoder.decode(messageParts[0], StandardCharsets.UTF_8));
        String data = URLDecoder.decode(messageParts[1],StandardCharsets.UTF_8);
        Request request = new Request();
        request.setRequestType(requestType);
        request.setData(data);
        return request;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestType=" + requestType +
                ", data='" + data + '\'' +
                '}';
    }
}
