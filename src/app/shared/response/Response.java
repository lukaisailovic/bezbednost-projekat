package app.shared.response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Response {

    private ResponseType responseType;
    private String data = "";

    public Response() {
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

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public void send(PrintWriter out){
        String stringBuilder = URLEncoder.encode(this.responseType.toString(), StandardCharsets.UTF_8) +
                "." +
                URLEncoder.encode(this.data, StandardCharsets.UTF_8);
        out.println(stringBuilder);
    }

    public static Response receive(BufferedReader in) throws IOException {
        String message = in.readLine();
        String[] messageParts = message.split("\\.");
        ResponseType responseType = ResponseType.valueOf(URLDecoder.decode(messageParts[0], StandardCharsets.UTF_8));
        String data = URLDecoder.decode(messageParts[1],StandardCharsets.UTF_8);
        Response response = new Response();
        response.setResponseType(responseType);
        response.setData(data);
        return response;
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseType=" + responseType +
                ", data='" + data + '\'' +
                '}';
    }
}
