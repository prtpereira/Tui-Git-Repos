package com.tui.gitmanager.services;

import com.tui.gitmanager.model.Quote;
import com.tui.gitmanager.util.EnvVarFetcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class QuoteGardenAPIServiceImpl implements QuoteGardenAPIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public static final String QUOTE_GARDEN_ENDPOINT = EnvVarFetcher.getEnvStrElseDefault("QUOTE_GARDEN_ENDPOINT", "https://quote-garden.onrender.com/api/v3/quotes");

    public QuoteGardenAPIServiceImpl() {
        super();
    }

    @Override
    public Page<Quote> getQuotesByAuthor(String author, Pageable page) throws Exception {
        String result = restTemplate.getForObject(QUOTE_GARDEN_ENDPOINT + "?author=" + author +
                "&page=" + page.getPageNumber() + "&limit=" + page.getPageSize(), String.class);

        JSONObject response = new JSONObject(result);
        int statusCode = response.getInt("statusCode");

        if (statusCode < 200 || statusCode >= 300)  {
            throw new Exception("error trying to fetch data from Quote Garden API");
        } else {

            JSONArray quotesJSON = response.getJSONArray("data");
            List<Quote> quotes = new ArrayList<>();

            quotesJSON.iterator().forEachRemaining(element -> {
                JSONObject quote = (JSONObject) element;

                quotes.add(new Quote(null,
                                        quote.getString("_id"),
                                        quote.getString("quoteText"),
                                        quote.getString("quoteAuthor"),
                                        quote.getString("quoteGenre"),
                                        quote.getInt("__v")) );
            });

            return new PageImpl<>(quotes, page, quotes.size());
        }
    }

}
