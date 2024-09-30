package dtm.request_actions;

import dtm.request_actions.http.core.HttpActionGet;
import dtm.request_actions.http.implementation.HttpActionImpl;

public class Main {
    public static void main(String[] args) throws Exception{
        HttpActionGet actionGet = new HttpActionImpl((r) -> {
            System.out.println(r.statusCode());
        });
        var result = actionGet.get("https://www.google.com");
    }
}