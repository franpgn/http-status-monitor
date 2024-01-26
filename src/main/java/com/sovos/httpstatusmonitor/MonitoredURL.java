package com.sovos.httpstatusmonitor;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Properties;

public class MonitoredURL {
    static public int monitorRequestStatusCode(String url) throws InterruptedException, URISyntaxException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        if (!url.contains("https")) {
            url = String.format("http://%s", url);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .timeout(Duration.ofSeconds(100))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode();
    }

    static public void sendEmailAlert(String urlName, int statusCode) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("httpstatusmonitor@gmail.com", "ovmhqcevabxnsswj");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("httpstatusmonitor@gmail.com"));

            Address[] toUser = InternetAddress
                    .parse("httpstatusmonitor@gmail.com, francescogalvao@gmail.com");

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("Alert from MonitorWatch");
            String messageText = getString(urlName, statusCode);
            message.setContent(messageText, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getString(String urlName, int statusCode) {
        String messageText;
        if(statusCode >= 400 && statusCode < 500)
            messageText = "\uD83D\uDEA8 The monitored url " + urlName + " has returned a <strong>400 status code</strong> and may be down";
        else if (statusCode >= 500) {
            messageText = "\uD83D\uDEA8 The monitored url " + urlName + " has returned a <strong>500 status code</strong> and may be down";
        } else
            messageText = "\uD83D\uDEA8 The monitored url " + urlName + " has returned a different status code then success and may be down";
        return messageText;
    }
}
