package com.distributedsystems;

import com.distributedsystems.loadbalancer.dao.ClientReadRequest;
import com.distributedsystems.loadbalancer.dao.ClientWriteRequest;
import com.distributedsystems.loadbalancer.dao.ReadResponse;
import com.distributedsystems.loadbalancer.dao.WriteResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

public class TestClient {
    private static class HttpFailureException extends Exception{
        private int statusCode;
        private String responseString;
        private String url;
        public HttpFailureException(String url, int code, String response){
            super("Got status code " + String.valueOf(code) + " for url " + url + ". Response body: " + response);
            statusCode = code;
            responseString = response;
            this.url = url;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getResponseString(){
            return responseString;
        }

        public String getUrl(){
            return url;
        }
    }
    private static String getResponse(String path, Object requestPOJO) throws IOException, HttpFailureException {
        URL url = new URL("http://localhost:8080" + path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        os.write(gson.toJson(requestPOJO).getBytes());
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        if (con.getResponseCode() != HttpURLConnection.HTTP_OK){
            throw new HttpFailureException(url.toString(), con.getResponseCode(), response.toString());
        }
        return response.toString();
    }

    private static ReadResponse readValue(ClientReadRequest clientReadRequest) throws IOException, HttpFailureException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String response = getResponse("/get", clientReadRequest);
        return gson.fromJson(response, ReadResponse.class);
    }

    private static WriteResponse writeValue(ClientWriteRequest clientWriteRequest) throws IOException, HttpFailureException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String response = getResponse("/put", clientWriteRequest);
        return gson.fromJson(response, WriteResponse.class);
    }

    private static class ReadTwiceTask implements Callable<String> {
        private int pid;
        public ReadTwiceTask(int pid){
            this.pid = pid;
        }

        @Override
        public String call() throws Exception {
            ClientReadRequest rr = new ClientReadRequest();
            rr.setKey("x");
            rr.setLamportClock(String.valueOf(pid)+".1");
            ReadResponse resp1 = readValue(rr);
            if(resp1.getValue().length()==0){
                resp1.setValue("<null>");
            }
            ReadResponse resp2 = readValue(rr);
            if(resp2.getValue().length()==0){
                resp2.setValue("<null>");
            }
            return "Process " + String.valueOf(pid) +" Reading x: call 1 got \"" + resp1.getValue() + "\" call 2 got \"" + resp2.getValue() + "\"";
        };
    }

    public static void main(String[] args) throws Exception {
        int noOfReadTasks = 5;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        CompletionService<String> service = new ExecutorCompletionService<String>(executor);
        service.submit(()->{
            ClientWriteRequest wr = new ClientWriteRequest();
            wr.setKey("x");
            wr.setValue("a");
            wr.setLamportClock("1.1");
            writeValue(wr);
            return "Process 1: setting value of x = a";
        });
        service.submit(()->{
            ClientWriteRequest wr = new ClientWriteRequest();
            wr.setKey("x");
            wr.setValue("b");
            wr.setLamportClock("2.1");
            writeValue(wr);
            return "Process 2: setting value of x = b";
        });

        for(int i=0; i<noOfReadTasks; i++){
            ReadTwiceTask readTwiceTask = new ReadTwiceTask(i+3);
            service.submit(readTwiceTask);
        }
        for(int i=0; i<noOfReadTasks+2; i++){
            Future<String> f = service.take();
            try {
                System.out.printf("%s\n", f.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(1000);
        System.out.printf("Checking after 1 second\n%s\n", new ReadTwiceTask(100).call());
        executor.shutdownNow();
    }
}
