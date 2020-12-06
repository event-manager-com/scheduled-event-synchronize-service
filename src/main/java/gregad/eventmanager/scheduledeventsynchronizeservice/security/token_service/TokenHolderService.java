package gregad.eventmanager.scheduledeventsynchronizeservice.security.token_service;

/**
 * @author Greg Adler
 */
public interface TokenHolderService {
    void refreshToken();
    String getToken();
}
