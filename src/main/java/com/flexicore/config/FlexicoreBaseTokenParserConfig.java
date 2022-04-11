package com.flexicore.config;

import com.flexicore.model.Baseclass;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FlexicoreBaseTokenParserConfig {

    @Value("${flexicore.security.jwt.secretLocation:/home/flexicore/jwt.secret}")
    private String jwtTokenSecretLocation;
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cachedJWTSecret")
    public String cachedJWTSecret(){
        return getJWTSecret();
    }



    private String getJWTSecret() {
        String cachedJWTSecret=null;
        File file=new File(jwtTokenSecretLocation);
        if(file.exists()){
            try {
                cachedJWTSecret= FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(cachedJWTSecret==null){
            cachedJWTSecret= Baseclass.getBase64ID();
            try {
                FileUtils.write(file,cachedJWTSecret,StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cachedJWTSecret;


    }

}
