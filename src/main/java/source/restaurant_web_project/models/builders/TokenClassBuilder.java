package source.restaurant_web_project.models.builders;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import source.restaurant_web_project.models.entity.Token;

import java.time.LocalDateTime;

@Component
public class TokenClassBuilder extends ClassBuilderPattern<Token> {

    public TokenClassBuilder(ModelMapper modelMapper) {
        super(modelMapper, new Token());
    }

    @Override
    public TokenClassBuilder mapToClass(Object object) {
        super.mapToClass(object);
        return this;
    }

    @Override
    public TokenClassBuilder cloneObject(Token entity) {
        super.cloneObject(entity);
        return this;
    }

    public TokenClassBuilder withToken(String token){
        super.entity.setToken(token);
        return this;
    }

    public TokenClassBuilder withEmail(String email){
        super.entity.setEmail(email);
        return this;
    }

    public TokenClassBuilder withExpireDate(LocalDateTime expireDate){
        super.entity.setExpiryDate(expireDate);
        return this;
    }

}
