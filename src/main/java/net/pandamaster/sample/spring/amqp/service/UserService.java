package net.pandamaster.sample.spring.amqp.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import net.pandamaster.sample.spring.amqp.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @PersistenceContext(unitName = "sample")
    EntityManager entityManager;

    @Autowired
    private AmqpAdmin sampleAmqpAdmin;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users;
        try {
            users = entityManager.createNamedQuery("Users.findByUsername", Users.class).setParameter("username", username).getSingleResult();
        }
        catch(NonUniqueResultException | NoResultException ex) {
            LOGGER.error(ex.getMessage(), ex.getCause(), ex);
            users = new Users();
        }
        return users;
    }
    
    public boolean login(String username, String password) {
        boolean result = true;

        try
        {
            UserDetails user = loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch (Exception ex)
        {
          LOGGER.error(ex.getMessage(), ex.getCause(), ex);

          result = false;
        }

        return result;
    }
    
    public void addQueue(String queueName) {
        Queue queue = QueueBuilder.durable(queueName).build();
        sampleAmqpAdmin.declareQueue(queue);
    }
}
