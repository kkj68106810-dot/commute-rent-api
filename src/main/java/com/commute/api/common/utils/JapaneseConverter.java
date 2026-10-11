package com.commute.api.common.utils;


import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * クラスの説明を記入する。
 * Write the class description.
 *
 * @author Kim Gwangjin
 * @since 2026/10/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JapaneseConverter {

    private final Tokenizer tokenizer;

    /**
     * 処理内容を記入する。
     * Write what this method does.
     *
     * @param name 説明 / Description
     * @return 戻り値の説明 / Return value description 0번 카타카나 1번 영어
     */
    public String convertKanjiToKatakana(String inputStr) {
        
        log.info("inputStr: {}", inputStr);
        if(inputStr == null || inputStr.trim().isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        List<Token> tokens = tokenizer.tokenize(inputStr);

        for (Token token : tokens) {
            String reading = "";
            log.info("readingStr : {}", reading);
            if(token.isKnown()) {
                reading = token.getReading();
            } else {
                reading = token.getSurface();
            }

            // 예외 처리
            if(reading != null && !reading.isEmpty()) {
                sb.append(reading);
            } else {
                sb.append(convertKanjiToKatakana(token.getSurface()));
            }
        }

        log.info("return: {}", sb.toString());
        return sb.toString();
    }

    /**
     * 処理内容を記入する。
     * Write what this method does.
     *
     * @param name 説明 / Description
     * @return 戻り値の説明 / Return value description
     */
    public String convertKatakanaToEn(String inputStr) {
        Transliterator transliterator = Transliterator.getInstance("Katakana-Latin");
        return transliterator.transliterate(inputStr);
    } 
}
