package kz.epam.InternetShop.security.oauth2.user;

import java.util.Map;

import static kz.epam.InternetShop.util.ConstantUtil.*;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get(PROVIDER_ID);
    }

    @Override
    public String getName() {
        return (String) attributes.get(PROVIDER_NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(PROVIDER_EMAIL);
    }

    @Override
    public String getGender() {
        return (String) attributes.get(PROVIDER_GENDER);
    }

    @Override
    public String getLocale() {
        return (String) attributes.get(PROVIDER_LOCALE);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(PROVIDER_PICTURE);
    }
}
