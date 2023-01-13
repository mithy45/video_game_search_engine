package fr.lernejo.search.api;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class GameController {
    private final RestHighLevelClient restHighLevelClient;

    public GameController(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @GetMapping("/api/games")
    public ArrayList<Object> GetGameList(@RequestParam(value = "query") String query) throws IOException
    {
        ArrayList<Object> games = new ArrayList<Object>();
        this.restHighLevelClient.search(new SearchRequest().source(SearchSourceBuilder.searchSource().query(new QueryStringQueryBuilder(query))),
                RequestOptions.DEFAULT).getHits().forEach(hit -> games.add(hit.getSourceAsMap()));
        return games;
    }
}
