package gregad.eventmanager.scheduledeventsynchronizeservice.dao;

import gregad.eventmanager.scheduledeventsynchronizeservice.model.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Greg Adler
 */
public interface EventDao extends MongoRepository<EventEntity,Long> {
    List<EventEntity>findAllByEventDateBeforeAndEventTimeBefore(LocalDate date, LocalTime time);
}
