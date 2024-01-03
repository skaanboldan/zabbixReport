package com.producer.zabbix.report.zabbixReportProducer.ZabbixController;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "/zabbix/teams")
public class zabbixController {

    @GetMapping
    public String hello() {
            // Zabbix API URL'si
                String apiUrl = "192.168.0.193/zabbix/api_jsonrpc.php";

            // Zabbix kullanıcı adı ve şifresi
            String username = "Admin";
            String password = "zabbix";

            // Zabbix API anahtarınız
            //String apiKey = authenticate(apiUrl, username, password);

                // Zabbix API anahtarı ile istek yapabilirsiniz
                String response = makeApiRequest(apiUrl, "5d348e052eb29421b2ca423ba3aacc8b55cf4d45e2148bfe3366bdab39b3a853", "apiinfo.version");
                System.out.println("Zabbix API Cevabı: " + response);

        return apiUrl;
    }

        private static String authenticate(String apiUrl, String username, String password) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.addHeader("Content-Type", "application/json");

                // Zabbix API kimlik doğrulama isteği için JSON verisi
                String requestBody = String.format(
                        "{\"jsonrpc\":\"2.0\",\"method\":\"user.login\",\"params\":{\"user\":\"%s\",\"password\":\"%s\"},\"id\":1}",
                        username, password);

                httpPost.setEntity(new org.apache.http.entity.StringEntity(requestBody));

                // HTTP isteğini gönder
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = (HttpEntity) response.getEntity();

                if (entity != null) {
                    // Yanıtı oku ve API anahtarını al
                    return EntityUtils.toString((org.apache.http.HttpEntity) entity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private static String makeApiRequest(String apiUrl, String apiKey, String method) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.addHeader("Content-Type", "application/json");

                // Zabbix API isteği için JSON verisi
                String requestBody = String.format(
                        "{\"jsonrpc\":\"2.0\",\"method\":\"%s\",\"auth\":\"%s\",\"id\":1}",
                        method, apiKey);

                httpPost.setEntity(new org.apache.http.entity.StringEntity(requestBody));

                // HTTP isteğini gönder

                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = (HttpEntity) response.getEntity();

                if (entity != null) {
                    // Yanıtı oku
                    return EntityUtils.toString((org.apache.http.HttpEntity) entity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


