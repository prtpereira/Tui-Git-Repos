package com.tui.gitmanager.services;

import com.tui.gitmanager.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface QuotesService {

    public Optional<Quote> getQuoteByExternalId(String externalId);

    public Page<Quote> getQuotesByAuthor(String author, Pageable page) throws Exception;

    public Page<Quote> getQuotes(Pageable page);

    public Quote saveQuote(Quote quote);
}
