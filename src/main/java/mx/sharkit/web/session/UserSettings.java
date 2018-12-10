package mx.sharkit.web.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author asalgado
 */
@Component
@Scope("session")
public class UserSettings implements Serializable {

    private static final long serialVersionUID = 20111020L;

    @Value("${mx.sharkit.aplication.primefacesTheme}")
    private String primefacesTheme;

    String contextPath;
    String cookieName;
    String cookieNameColorTheme;
    String cookieLanguage;

    private List<String> availableThemes;
    private String currentTheme;

    @PostConstruct
    public void init() {
        contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        cookieName = "primefaces-theme-" + contextPath.substring(1);
        cookieNameColorTheme = "color-theme-" + contextPath.substring(1);
        cookieLanguage = "language-" + contextPath.substring(1);

        availableThemes = loadAvailableThemes();

        Map<String, Object> mapCookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        if (mapCookies != null && mapCookies.get(cookieName) != null) {
            Cookie cookie = (Cookie) mapCookies.get(cookieName);
            currentTheme = cookie.getValue();
        } else if (primefacesTheme != null) {
            currentTheme = primefacesTheme;
        } else {
            currentTheme = "omega";
        }

    }

    public final void setCurrentTheme(String currentTheme) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("path", contextPath);
        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie(
                cookieName, currentTheme, properties);
        this.currentTheme = currentTheme;
    }

    public final List<String> getAvailableThemes() {
        return availableThemes;
    }

    public final void setAvailableThemes(List<String> availableThemes) {
        this.availableThemes = availableThemes;
    }

    public final String getCurrentTheme() {
        return currentTheme;
    }

    private List<String> loadAvailableThemes() {
        List<String> avThemes = new ArrayList<>();
        avThemes.add("afterdark");
        avThemes.add("afternoon");
        avThemes.add("afterwork");
        avThemes.add("afterdark");
        avThemes.add("aristo");
        avThemes.add("black-tie");
        avThemes.add("blitzer");
        avThemes.add("bluesky");
        avThemes.add("bootstrap");
        avThemes.add("casablanca");
        avThemes.add("cupertino");
        avThemes.add("cruze");
        avThemes.add("dark-hive");
        avThemes.add("dot-luv");
        avThemes.add("eggplant");
        avThemes.add("excite-bike");
        avThemes.add("flick");
        avThemes.add("glass-x");
        avThemes.add("home");
        avThemes.add("hot-sneaks");
        avThemes.add("humanity");
        avThemes.add("le-frog");
        avThemes.add("midnight");
        avThemes.add("mint-choc");
        avThemes.add("omega");
        avThemes.add("overcast");
        avThemes.add("pepper-grinder");
        avThemes.add("redmond");
        avThemes.add("rocket");
        avThemes.add("sam");
        avThemes.add("smoothness");
        avThemes.add("south-street");
        avThemes.add("start");
        avThemes.add("sunny");
        avThemes.add("swanky-purse");
        avThemes.add("trontastic");
        avThemes.add("ui-darkness");
        avThemes.add("ui-lightness");
        avThemes.add("vader");
        avThemes.add("mytheme");
        return avThemes;
    }

}
