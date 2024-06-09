package source.restaurant_web_project.intercaptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Locale.getDefault() != request.getLocale()) {
            Locale.setDefault(request.getLocale());
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
