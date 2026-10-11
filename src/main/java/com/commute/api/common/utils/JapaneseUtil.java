package com.commute.api.common.utils;

import com.atilika.kuromoji.ipadic.Tokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class JapaneseUtil {

    @Bean
    public Tokenizer tokenizer() {
        return new Tokenizer();
    }

}
