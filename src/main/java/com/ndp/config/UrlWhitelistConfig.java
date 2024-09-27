package com.ndp.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "url-whitelist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlWhitelistConfig {

    List<String> urls;
}
