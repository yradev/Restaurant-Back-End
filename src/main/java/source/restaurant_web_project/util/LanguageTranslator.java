package source.restaurant_web_project.util;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.dto.TranslationDTO;

import java.util.Locale;

@Component
public class LanguageTranslator {
    private final Gson gson;

    public LanguageTranslator(Gson gson) {
        this.gson = gson;
    }

    public String translateToJSON(String message, String json) {
        String locale = Locale.getDefault().toString();

        TranslationDTO translationDTO;

        if(json!=null) {
            translationDTO = gson.fromJson(json, TranslationDTO.class);
        }else{
            translationDTO = new TranslationDTO();
        }

        switch (locale) {
            case "bg" -> translationDTO.setBg_BG(message);
            default -> translationDTO.setEn_EN(message);
        }

        return gson.toJson(translationDTO);    }

    public String translateFromJson(String json) {
        if(json == null || json.isEmpty()){
            return null;
        }

        TranslationDTO translationDTO = gson.fromJson(json,TranslationDTO.class);

        String locale = Locale.getDefault().toString();


        return switch (locale) {
            case "bg" -> translationDTO.getBg_BG() != null ? translationDTO.getBg_BG() : "";
            default -> translationDTO.getEn_EN() != null ? translationDTO.getEn_EN() : "";
        };
    }
}
