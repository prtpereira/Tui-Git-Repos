package com.tui.gitmanager.services;

import com.tui.gitmanager.model.Quote;
import com.tui.gitmanager.repositories.QuoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class QuotesServiceImpl implements QuotesService {

    private final QuoteRepository quoteRepository;
    private final QuoteGardenAPIService quoteGardenAPIService;

    private static AtomicInteger itemsInCollectionCounter = new AtomicInteger(0);

    private final int MAX_NUM_ITEMS_COLLECTION = 50_000;

    public QuotesServiceImpl(QuoteRepository quoteRepository, QuoteGardenAPIService quoteGardenAPIService) {
        this.quoteRepository = quoteRepository;
        this.quoteGardenAPIService = quoteGardenAPIService;
    }

    @Override
    public Optional<Quote> getQuoteByExternalId(String id) {

        return quoteRepository.findByExternalId(id);
    }

    @Override
    public Page<Quote> getQuotesByAuthor(String author, Pageable page) throws Exception {

        Page<Quote> quotes = quoteRepository.findByAuthor(author, page);

        if (quotes.isEmpty()) {

            quotes = quoteGardenAPIService.getQuotesByAuthor(author, page);
            List<Quote> quotesList = quotes.toList();

            for(int i = 0; i < quotesList.size() && itemsInCollectionCounter.get() < MAX_NUM_ITEMS_COLLECTION; i++) {
                Quote currentQuote = quotesList.get(i);
                String currentExternalId = currentQuote.getExternalId();

                if (quoteRepository.findByExternalId( currentExternalId ).isEmpty()) {
                    quoteRepository.save( currentQuote );
                    itemsInCollectionCounter.incrementAndGet();
                }
            }
        }

        return quotes;
    }

    @Override
    public Page<Quote> getQuotes(Pageable page) {

        return quoteRepository.findAll(page);
    }

    @Override
    public Quote saveQuote(Quote quote) {

        return quoteRepository.save(quote);
    }

}
