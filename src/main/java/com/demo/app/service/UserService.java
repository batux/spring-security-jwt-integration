package com.demo.app.service;

import com.demo.app.model.UserContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class UserService {

    private final Object mutex = new Object();
    private List<UserContext> userContextList = Collections.synchronizedList(new ArrayList<>());

    @PostConstruct
    public void init() {

        userContextList.add(new UserContext(1, "Mustafa Kemal", "Atatürk", "mustafa.kemal.ataturk@turkiye.com.tr", "1881", "ADMIN"));
        userContextList.add(new UserContext(2, "Hasan Ali", "Yücel", "hasan.ali.yucel@turkiye.com.tr", "1897", "ADMIN"));
    }

    public Integer save(UserContext userContext) {

        synchronized (mutex) {
            Integer userId = determineMaxUserId();
            userContext.setId(userId);
            userContextList.add(userContext);
        }
        return userContext.getId();
    }

    public UserContext load(String email) {

        synchronized (mutex) {
            return userContextList.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        }
    }

    public UserContext load(Integer id) {

        synchronized (mutex) {
            return userContextList.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        }
    }

    private Integer determineMaxUserId() {

        int maxUserId = 1;
        UserContext lastUserContext = userContextList.stream().max(Comparator.comparing(UserContext::getId)).orElse(null);
        if(lastUserContext != null) {
            maxUserId = lastUserContext.getId() + 1;
        }
        return maxUserId;
    }
}
