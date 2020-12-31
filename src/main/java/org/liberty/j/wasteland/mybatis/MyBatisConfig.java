package org.liberty.j.wasteland.mybatis;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class MyBatisConfig {
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    public String getUrl()
    {
        return url;
    }
    public void setUrl(String Url)
    {
        this.url = Url;
    }
    public String getDriverClassName()
    {
        return driverClassName;
    }
 
    public void setDriverClassName(String driverClassName)
    {
        this.driverClassName = driverClassName;
    }
 
    public String getUsername()
    {
        return username;
    }
 
    public void setUsername(String username)
    {
        this.username = username;
    }
 
    public String getPassword()
    {
        return password;
    }
 
    public void setPassword(String password)
    {
        this.password = password;
    }
}