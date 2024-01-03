package com.producer.zabbix.report.zabbixReportProducer.ZabbixController;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

@RestController
public class ReportController {

    @CrossOrigin
    @RequestMapping(path = "/zabbix/report/getActiveHost")
    public static ArrayList<String> deneme() throws UnsupportedEncodingException {
        Properties prop = null;
        String ZABBIX_API_URL = null;
        JSONObject jsonObj = null;
        String[]  hostArray= null;

        ArrayList<String> notAvailableList=new ArrayList<String>();

        for (String s : hostArray) {


            try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {

                prop = new Properties();

                // load a properties file
                prop.load(input);

                // get the property value and print it out
                System.out.println(prop.getProperty("zabbix.ip"));
                ZABBIX_API_URL = "http://" + prop.getProperty("zabbix.ip") + "/zabbix/api_jsonrpc.php";

                jsonObj = new JSONObject("{\n" +
                        "           \"jsonrpc\": \"2.0\",\n" +
                        "           \"method\": \"host.get\",\n" +
                        "           \"params\": {\n" +
                        "               \"filter\": {\n" +
                        "                   \"host\": [\n" +
                        "                       " + s +
                        "                   ]\n" +
                        "               }\n" +
                        "           },\n" +
                        "           \"auth\": \"" + prop.getProperty("zabbix.apikey") + "\",\n" +
                        "           \"id\": 1\n" +
                        "       }");

            } catch (IOException ex) {
                System.err.println(s+" error occured..");
                ex.printStackTrace();
            }

            HttpClient client = new HttpClient();

            PutMethod putMethod = new PutMethod(ZABBIX_API_URL);
            System.err.println(putMethod.getName());
            putMethod.setRequestHeader("Content-Type", "application/json-rpc"); // content-type is controlled in api_jsonrpc.php, so set it like this
            ArrayList<ZabbixReport> zabbixReports = new ArrayList<ZabbixReport>();
            putMethod.setRequestBody(fromString(jsonObj.toString())); // put the json object as input stream into request body
            InputStream apiResponse;
            try {
                client.executeMethod(putMethod); // send to request to the zabbix api
                apiResponse = putMethod.getResponseBodyAsStream(); // read the result of the response
                System.out.println(apiResponse.toString() +'\n');
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(apiResponse));
                StringBuilder responseStringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    responseStringBuilder.append(line);

                }
                String responseString = responseStringBuilder.toString();
                System.out.println(responseString);


                if(responseString.contains("{\"jsonrpc\":\"2.0\",\"result\":[],\"id\":1}"))
                {
                    System.err.println(s+" yok");
                    notAvailableList.add(s);
                }



                //return zabbixReports;

            } catch (HttpException e) {
                e.printStackTrace();
                System.err.println(s+" error occured.. on HttpException");

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(s+" error occured.. on IOException");

            }
            //return null;
        }

        return notAvailableList;
    }



    @CrossOrigin
    @GetMapping
    @RequestMapping(path = "/zabbix/report/getProblems")
    public static ArrayList<ZabbixReport> test() throws UnsupportedEncodingException {
        Properties prop = null;
        String ZABBIX_API_URL = null;
        JSONObject jsonObj = null;
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {

            prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("zabbix.ip"));
            ZABBIX_API_URL = "http://" + prop.getProperty("zabbix.ip") + "/zabbix/api_jsonrpc.php";
            jsonObj = new JSONObject("{\n" +
                    "           \"jsonrpc\": \"2.0\",\n" +
                    "           \"method\": \"problem.get\",\n" +
                    "           \"params\": {\n" +
                    "               \"output\": \"extend\",\n" +
                    "               \"selectAcknowledges\": \"extend\",\n" +
                    "               \"selectTags\": \"extend\",\n" +
                    "               \"selectSuppressionData\": \"extend\",\n" +
                    "               \"recent\": \"true\",\n" +
                    "               \"sortfield\": [\"eventid\"],\n" +
                    "               \"sortorder\": \"DESC\"\n" +
                    "           },\n" +
                    "           \"auth\": \"" + prop.getProperty("zabbix.apikey") + "\",\n" +
                    "           \"id\": 1\n" +
                    "       }");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        org.apache.commons.httpclient.HttpClient client = new HttpClient();

        PutMethod putMethod = new PutMethod(ZABBIX_API_URL);
        System.err.println(putMethod.getName());
        putMethod.setRequestHeader("Content-Type", "application/json-rpc"); // content-type is controlled in api_jsonrpc.php, so set it like this
        ArrayList<ZabbixReport> zabbixReports=new ArrayList<ZabbixReport>();
        putMethod.setRequestBody(fromString(jsonObj.toString())); // put the json object as input stream into request body
        String apiResponse = "";
        try {
            client.executeMethod(putMethod); // send to request to the zabbix api
            apiResponse = putMethod.getResponseBodyAsString(); // read the result of the response
            JSONObject obj = new JSONObject(apiResponse);
            JSONArray arr = obj.getJSONArray("result"); // notice that `"posts": [...]`
            ArrayList < String > hostId = new ArrayList < > ();

            for (int i = 0; i < arr.length(); i++) {
                ZabbixReport zr =new ZabbixReport();
                zr.setReportName(arr.getJSONObject(i).getString("name"));
                zabbixReports.add(zr);
                hostId.add(arr.getJSONObject(i).getString("name"));

            }

            return zabbixReports;

        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static InputStream fromString(String str) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes("UTF-8");
        return new ByteArrayInputStream(bytes);
    }
}