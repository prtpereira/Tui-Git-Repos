package com.tui.gitmanager.utils;

import com.tui.gitmanager.model.GitRepository;

import java.util.HashMap;
import java.util.List;

public class DataMock {

    public static List<GitRepository> getMockedRepositoriesPrtpereira() {
        HashMap<String, String > branches1 = new HashMap<>();
        branches1.put("master", "b1bcfa93a596b75e109f3c41201998f582b51cab");
        branches1.put("test", "b1bcfa93a596b75e109f3c41201998f582b51cab");

        HashMap<String, String > branches2 = new HashMap<>();
        branches2.put("master", "2f545527abeb2c07c15c194d8c8f8c73e4b1a018");

        HashMap<String, String > branches3 = new HashMap<>();
        branches3.put("master", "34ef7e69112ee9a0a9be643d89b0ab0bebd19873");

        final List<GitRepository> repositories = List.of(new GitRepository("-C-w-pointers-manipulation-", branches1),
                new GitRepository("BigTwo", branches2),
                new GitRepository("Concurrent-Game", branches3));

        return repositories;
    }
}
