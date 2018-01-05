package ru.sql.proj;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.io.File;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("10.0.2.22");
        sf.setPort(21);
        sf.setUsername("ftpuser");
        sf.setPassword("3333");
        return new CachingSessionFactory<FTPFile>(sf);
    }

    @Bean
    public FtpInboundFileSynchronizer ftpInboundFileSynchronizer() {
        FtpInboundFileSynchronizer fileSynchronizer = new FtpInboundFileSynchronizer(ftpSessionFactory());
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setRemoteDirectory("");
        fileSynchronizer.setFilter(new FtpSimplePatternFileListFilter("*.txt"));
        return fileSynchronizer;
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(1000));
        return pollerMetadata;
    }

    @Bean
    @InboundChannelAdapter(channel = "ftpChannel")
    public MessageSource<File> ftpMessageSource() {
        FtpInboundFileSynchronizingMessageSource source =
                new FtpInboundFileSynchronizingMessageSource(ftpInboundFileSynchronizer());
        source.setLocalDirectory(new File("ftp-inbound"));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<File>());
        return source;
    }


    @Bean
    @ServiceActivator(inputChannel = "ftpChannel")
    public MessageHandler handler() {
        return new MessageHandler() {


            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                Object payload = message.getPayload();
                if(payload instanceof File){
                    File f = (File) payload;
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!>>>>>>>>>>>>>>>>>>");
                    System.out.println(f.getName());
                }else{
                    System.out.println(message.getPayload());
                }
            }

        };
    }

}
