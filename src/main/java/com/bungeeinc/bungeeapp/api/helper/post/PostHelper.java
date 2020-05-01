package com.bungeeinc.bungeeapp.api.helper.post;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class PostHelper {

    public static Set<String> findTags(String post) {
        StringTokenizer tokenizer = new StringTokenizer(post);
        Set<String> tags = new HashSet<>();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.startsWith("#")) {
                tags.add(token);
            }
        }
        return tags;
    }

}
