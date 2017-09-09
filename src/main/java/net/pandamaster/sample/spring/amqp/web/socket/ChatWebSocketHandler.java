package net.pandamaster.sample.spring.amqp.web.socket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.pandamaster.sample.spring.amqp.web.json.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class ChatWebSocketHandler extends AbstractWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ChatMessage json = null;
        try {
            json = mapper.readValue(message.getPayload(), ChatMessage.class);
            if(json.getFrom().equals(session.getAttributes().get("queueName"))) {
                json.setFrom(session.getPrincipal().getName());
            }
            else {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("User unmatched."));
            }
        }
        catch(JsonParseException ex) {
            LOGGER.debug("Non-json text message.");
            session.sendMessage(message);
        }
        catch(JsonProcessingException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
        }
        catch(IOException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
        }
    }
}
