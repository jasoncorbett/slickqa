package org.tcrun.slickij.events;

import com.google.code.morphia.query.Query;
import com.google.inject.Inject;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcrun.slickij.api.SystemConfigurationResource;
import org.tcrun.slickij.api.data.AMQPSystemConfiguration;
import org.tcrun.slickij.api.data.Configuration;
import org.tcrun.slickij.api.data.dao.ConfigurationDAO;
import org.tcrun.slickij.api.events.*;

import java.util.HashMap;
import java.util.List;

/**
 * User: jcorbett
 * Date: 11/30/12
 * Time: 2:18 PM
 */
public class AMQPEventManager implements EventManager
{
    private AMQPSystemConfiguration config;
    private SystemConfigurationResource configLoader;
    private Connection amqpConnection;
    private SystemStatus currentSystemStatus;

    private static Logger logger = LoggerFactory.getLogger(AMQPEventManager.class);

    @Inject
    public AMQPEventManager(SystemConfigurationResource configLoader)
    {
        currentSystemStatus = SystemStatus.NOT_CONFIGURED;
        this.configLoader = configLoader;
        reload();
    }

    private void loadConfiguration()
    {
        List<AMQPSystemConfiguration> configs = configLoader.getMatchingConfigurationsOfType(AMQPSystemConfiguration.class, null, AMQPSystemConfiguration.CONFIGURATION_TYPE_NAME);
        if(configs.size() == 1)
        {
            config = configs.get(0);
        } else if(configs.size() > 1)
        {
            config = configs.get(0);
            logger.warn("There are {} AMQPSystemConfigurations saved, only using the first one.", configs.size());
        }
    }

    public void reload()
    {
        if(amqpConnection != null)
        {
            logger.debug("Closing existing, then reloading the amqp connection");
            try
            {
                amqpConnection.close();
            } catch (Exception ex)
            {
                logger.warn("Tried to close the amqp connection, but failed: " + ex.getMessage(), ex);
            }
        }
        currentSystemStatus = SystemStatus.NOT_CONFIGURED;
        loadConfiguration();
        if(config != null)
        {
            try
            {
                StringBuilder settings = new StringBuilder();
                settings.append("Using ");
                ConnectionFactory factory = new ConnectionFactory();
                if(config.getUsername() != null)
                {
                    settings.append("username='");
                    settings.append(config.getUsername());
                    settings.append("' ");
                    factory.setUsername(config.getUsername());
                }
                if(config.getPassword() != null)
                {
                    settings.append("password='");
                    settings.append(config.getPassword());
                    settings.append("' ");
                    factory.setPassword(config.getPassword());
                }
                if(config.getVirtualHost() != null)
                {
                    settings.append("virtualHost='");
                    settings.append(config.getVirtualHost());
                    settings.append("' ");
                    factory.setVirtualHost(config.getVirtualHost());
                }
                settings.append("hostname='");
                settings.append(config.getHostname());
                settings.append("' ");
                factory.setHost(config.getHostname());
                settings.append("port='");
                settings.append(config.getPort());
                settings.append("'.");
                factory.setPort(config.getPort());

                logger.debug(settings.toString());

                amqpConnection = factory.newConnection();
                logger.info("Successfully connected to amqp server.");
                if (config.getExchangeName() == null || config.getExchangeName().equals(""))
                {
                    logger.debug("No exchange name given, using amqp.topic as the exchange name.");
                    config.setExchangeName("amqp.topic");
                }

                currentSystemStatus = SystemStatus.CONFIGURED;

                //TODO: re-attach any listeners
            } catch(Exception ex)
            {
                logger.warn("Slick event subsystem encountered an error while initializing: " + ex.getMessage(), ex);
                currentSystemStatus = SystemStatus.ERROR;
            }
        } else
        {
            logger.info("Slick event subsystem not initialized, no configuration available.");
        }
    }

    @Override
    public SystemStatus getSystemStatus()
    {
        return currentSystemStatus;
    }

    @Override
    public void publishMessage(RawMessage message)
    {
        if(amqpConnection != null)
        {
            try
            {
                Channel chan = amqpConnection.createChannel();
                chan.basicPublish(config.getExchangeName(), message.getTopic(), new AMQP.BasicProperties.Builder().contentType(message.getContentType())
                                                                                .contentEncoding(message.getEncoding())
                                                                                .headers(new HashMap<String, Object>(message.getHeaders()))
                                                                                .deliveryMode(2).build(),
                                  message.getContent());
                chan.close();
            } catch(Exception ex)
            {
                logger.warn("Unable to publish event: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void publishEvent(SlickEvent event)
    {
        if(amqpConnection != null && event != null)
        {
            try
            {
                publishMessage(event.toRawMessage());
            } catch (SlickEventException ex)
            {
                logger.warn("Unable to send event, toRawMessage threw an exception: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void addMessageListener(QueueDefinition queue, MessageListener listener) throws SlickEventException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
