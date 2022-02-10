package com.example.pingservice;

import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@RestController
public class PingRestController {

    public static final Logger log = LoggerFactory.getLogger(PingRestController.class);

    @Value("${connection.Timeout}")
    private int connectionTimeout;


    @PostMapping("/api/ping")
    public ResponseEntity<String> getPing(@RequestBody PingRequest reqObj) throws IOException {
        log.info("Inside Ping Method with req obj value are :::::::: "+ reqObj.getScheme() +" " +reqObj.getHost() + " " + reqObj.getPort() +" "+ reqObj.getPath() + " "+ reqObj.getQuery());
        String urlConcatenated ="";
        int code =00;
        if(StringUtils.isNotBlank(reqObj.getScheme()) && StringUtils.isNotBlank(reqObj.getHost())){
            urlConcatenated = reqObj.scheme.concat("://").concat(reqObj.host);
        }
        if(StringUtils.isNotBlank(reqObj.getPort())){
            urlConcatenated = urlConcatenated.concat(":").concat(reqObj.getPort());
        }
        if(StringUtils.isNotBlank(reqObj.getPath())) {
            urlConcatenated = urlConcatenated.concat("/").concat(reqObj.getPath());
        }
        if(StringUtils.isNotBlank(reqObj.getQuery())){
            urlConcatenated = urlConcatenated.concat("?").concat(reqObj.getQuery());
        }

        log.info("Final Url created is :::::::: "+ urlConcatenated);
        code = getStatus(urlConcatenated);
        if(code == 200){
            return ResponseEntity.status(code).body("connectivity exist");
        } else {
            return ResponseEntity.status(code).body("connectivity failed");
        }
    }

    public int getStatus(String url) throws IOException {
        int code =00;
        try {
                    URL urlObj =  new URL(url);
                    HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                    con.setRequestMethod("GET");
                    // Set connection timeout
                    con.setConnectTimeout(connectionTimeout);
                    con.connect();
                    code = con.getResponseCode();
                    log.info("response code:::"+ code );

        } catch (Exception e) {
            log.error(e.getMessage());

    }
        return code;
}
    }

