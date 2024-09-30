package dtm.request_actions;

import dtm.request_actions.http.core.HttpActionGet;
import dtm.request_actions.http.implementation.HttpActionImpl;

public class Main {
    public static void main(String[] args) throws Exception{
        HttpActionGet actionGet = new HttpActionImpl();
        var result = actionGet.get("https://www.google.com");
        System.out.println(result.getBody().get());
    }
}