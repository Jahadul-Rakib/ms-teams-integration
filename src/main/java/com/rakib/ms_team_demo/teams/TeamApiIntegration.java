package com.rakib.ms_team_demo.teams;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class TeamApiIntegration {

    public static void sendMessage(String message) throws Exception {
        try {
            String accessToken = getAccessTokenByClientCredentialGrant().accessToken();
            sendMessageToGroup(accessToken, "hello");
            System.out.println("Users in the Tenant = " + accessToken);
        } catch (Exception ex) {
            System.out.println("Exception message - " + ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    private static IAuthenticationResult getAccessTokenByClientCredentialGrant() throws Exception {
        String authorityTenant = "https://login.microsoftonline.com/{tenant-id}/";
        String clientId = "";
        String clientSecret = "";

        ConfidentialClientApplication app = ConfidentialClientApplication
                .builder(clientId, ClientCredentialFactory.createFromSecret(clientSecret))
                .authority(authorityTenant)
                .build();
        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters
                .builder(Collections.singleton("https://graph.microsoft.com/.default"))
                .build();
        CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
        return future.get();
    }

    private static void sendMessageToGroup(String accessToken, String data) throws IOException {
        String teamId = "";
        String channelId = "";
        URL url = new URL("https://graph.microsoft.com/v1.0/teams/" + teamId + "/channels/" + channelId + "/messages");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();
        int httpResponseCode = conn.getResponseCode();
        System.out.println("Message Send: "+httpResponseCode);
    }
}
