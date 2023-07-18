
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot Starter Websocket [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-boot-starter-websocket%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot-starter-websocket/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-starter-websocket.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-starter-websocket%22)


For comprehensive information about FlexiCore Boot Starter Websocket please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Boot Starter Websocket is a FlexiCore Module that enables Java's Server Endpoint inside FlexiCore Plugins.

## How to use?
Add the flexicore-boot-starter-websocket dependency using the latest version available from maven central:

			<dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-boot-starter-websocket</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableFlexiCoreWebSocketPlugins

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreWebSocketPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a Websocket Server Endpoint inside a plugins:

    @ServerEndpoint(value = "/wsTest/{authenticationKey}", encoders = {WSEncoder.class}, decoders = {WSDecoder.class})  
    @PluginInfo(version = 1)  
    @Extension  
    @Component  
    public class TestWS implements Plugin {  
      
       private static final Logger logger=LoggerFactory.getLogger(TestWS.class);  
      
      @Autowired  
      private ApplicationEventPublisher eventReceivedContainerEvent;  
      
      @OnMessage  
      public void onMessage(WSEvent message, Session session) {  
          logger.info("received message " + message);  
      eventReceivedContainerEvent.publishEvent(new EventReceivedContainer()  
                .setWsEvent(message).setSession(session));  
      }  
      
       @OnOpen  
      public void open(@PathParam("authenticationKey") String authenticationKey,  
      Session session) {  
          logger.info("Opening:" + session.getId());  
     try {  
             session.getBasicRemote().sendObject(new TestMessage().setTest("test: " + session.getId()));  
      }  
          catch (Exception e){  
             logger.error("failed sending hello message");  
      }  
          UiEventSender.registerUISession(session);  
      }  
      
       @OnClose  
      public void close(@PathParam("authenticationKey") String authenticationKey,  
      CloseReason c, Session session) {  
          logger.info("Closing:" + session.getId());  
      UiEventSender.unregisterSession(session);  
      }  
      
    }

## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)

[FlexiCore Boot Starter Web](https://github.com/wizzdi/flexicore-boot-starter-web)

[Spring Boot Starter Websocket](https://search.maven.org/artifact/org.springframework.boot/spring-boot-starter-websocket)
