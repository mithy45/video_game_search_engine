package fr.lernejo.search.api;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GameInfoListener {

    private final RestHighLevelClient restHighLevelClient;

    public GameInfoListener(RestHighLevelClient restHighLevelClient)
    {
        this.restHighLevelClient = restHighLevelClient;
    }

    @RabbitListener(queues = AmqpConfiguration.GAME_INFO_QUEUE)
    void onMessage(String message, @Header("game_id") String gameId) throws IOException
    {
        IndexRequest request = new IndexRequest("games").id(gameId).source(message, XContentType.JSON);
        this.restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }
}
