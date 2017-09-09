package net.pandamaster.sample.spring.amqp.web.controller.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import net.pandamaster.sample.spring.amqp.web.json.ChatMessage;
import net.pandamaster.sample.spring.amqp.web.json.RoomEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRestController.class);
    
    @Autowired
    private AmqpAdmin sampleAmqpAdmin;
    
    @Autowired
    private RabbitTemplate sampleRabbitTemplate;
    
    @Autowired
    private DirectExchange sampleDirect;
    
    @RequestMapping(value="/api/enterroom", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
    public String enterRoom(@RequestBody String json, HttpServletRequest req, Principal principal, Locale locale) {
        String responseJson = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            RoomEntry room = mapper.readValue(json, RoomEntry.class);
            sampleAmqpAdmin.declareBinding(BindingBuilder.bind(new Queue((String)req.getSession().getAttribute("queueName"))).to(sampleDirect).with(room.getRoom()));
        }
        catch(JsonProcessingException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
        }
        catch(IOException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
        }
        return responseJson;
    }
    
    @MessageMapping("/queue")
    public void connectQueue(String jsonText, Principal principal) {
        
    }
    
    @MessageMapping("/message")
    public void sendMessage(String jsonText, Principal principal) {
        //LOGGER.debug(jsonText);
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        try {
            ChatMessage json = mapper.readValue(jsonText, ChatMessage.class);
            json.setFrom(principal.getName());
            json.setTime(sdf.format(new Date()));
            result = mapper.writeValueAsString(json);
            sampleRabbitTemplate.convertAndSend(sampleDirect.getName(), json.getTo(), result);
        }
        catch(JsonParseException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
        }
        catch(JsonProcessingException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
        }
        catch(IOException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
        }
    }
}
