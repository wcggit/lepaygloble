package com.jifenke.lepluslive.global.config.logging;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.config.logging.LoggingAspect;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
