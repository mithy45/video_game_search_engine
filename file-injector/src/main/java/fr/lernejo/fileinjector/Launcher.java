package fr.lernejo.fileinjector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) throws IOException {
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            if (args.length > 0) {
                List<GameInfo> gameInfos = Arrays.asList(new ObjectMapper().readValue(Paths.get(args[0]).toFile(), GameInfo[].class));
                RabbitTemplate rabbitTemplate = springContext.getBean(RabbitTemplate.class);
                for (GameInfo gameInfo : gameInfos) {
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    rabbitTemplate.convertAndSend("", "game_info", gameInfo, new MessagePostProcessor() {
                                @Override
                                public Message postProcessMessage(Message message) throws AmqpException {
                                    message.getMessageProperties().getHeaders().put("game_id", gameInfo.id());
                                    return message;
                                }});
                }
            }
        }
    }
}
