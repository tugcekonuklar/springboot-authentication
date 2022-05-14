package com.tkonuklar.springbootauthentication.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Data
public class FirebaseCredential {

    @Value("${firebase.credentials}")
    private String credentials;

    @Primary
    @Bean
    void initialiseAuthServer() throws IOException {
        InputStream credentialsStream = new ByteArrayInputStream(credentials.getBytes());
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialsStream)).build();
        FirebaseApp.initializeApp(options);
    }
}
