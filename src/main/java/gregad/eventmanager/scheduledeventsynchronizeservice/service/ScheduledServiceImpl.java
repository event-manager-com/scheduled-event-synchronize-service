package gregad.eventmanager.scheduledeventsynchronizeservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gregad.event_manager.loggerstarter.aspect.DoLogging;
import gregad.eventmanager.scheduledeventsynchronizeservice.dao.EventDao;
import gregad.eventmanager.scheduledeventsynchronizeservice.model.EventEntity;
import gregad.eventmanager.scheduledeventsynchronizeservice.security.token_service.TokenHolderService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import static gregad.eventmanager.scheduledeventsynchronizeservice.api.ApiConstants.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Greg Adler
 */
@Service
@DoLogging
public class ScheduledServiceImpl implements ScheduledService {
    private TokenHolderService tokenHolderService;
    private EventDao eventRepo;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @Value("${history.service.url}")
    private String historyServiceUrl;

    @Autowired
    public ScheduledServiceImpl(TokenHolderService tokenHolderService, EventDao eventRepo,
                                RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.tokenHolderService = tokenHolderService;
        this.eventRepo = eventRepo;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
    @Override
    @Scheduled(cron = "0 0 1 * * *")
    public void moveEvents() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        List<EventEntity> events = eventRepo.findAllByEventDateBeforeAndEventTimeBefore(date, time);
        if (events!=null && !events.isEmpty()){
            if (sendEvents(events)){
                eventRepo.deleteAll(events);
            }
        }
    }

    @SneakyThrows
    private boolean sendEvents(List<EventEntity> events) {
        org.springframework.http.HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HEADER,tokenHolderService.getToken());
        String jsonObj = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);

        ResponseEntity<Boolean> res = 
                restTemplate.postForEntity(historyServiceUrl, new HttpEntity<>(jsonObj, httpHeaders), Boolean.class);
        return res.getBody();
            
    }
}
