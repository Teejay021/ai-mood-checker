package com.aimoodchecker.controller;

import com.aimoodchecker.service.SentimentService;
import com.aimoodchecker.service.ChatGPTService;
import com.aimoodchecker.repository.EntryRepository;

public interface NeedsDeps {
    void init(EntryRepository repo, SentimentService sentiment, ChatGPTService chatGPT);
}
