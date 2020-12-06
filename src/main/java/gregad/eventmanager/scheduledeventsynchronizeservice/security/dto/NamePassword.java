package gregad.eventmanager.scheduledeventsynchronizeservice.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Greg Adler
 */
@Data
@AllArgsConstructor
public class NamePassword {
    private String name;
    private String password;
}
