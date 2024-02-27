package com.tui.gitmanager.services;

import com.tui.gitmanager.model.Quote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuoteGardenAPIService {

    public Page<Quote> getQuotesByAuthor(String author, Pageable page) throws Exception;
}
