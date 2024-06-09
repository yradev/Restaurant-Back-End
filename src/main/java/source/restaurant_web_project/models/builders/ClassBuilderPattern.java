package source.restaurant_web_project.models.builders;

import org.modelmapper.ModelMapper;

public abstract class ClassBuilderPattern<T> {
        protected T entity;
        private final ModelMapper modelMapper;

        protected ClassBuilderPattern(ModelMapper modelMapper, T entity){
            this.entity = entity;
            this.modelMapper = modelMapper;
        }

        public ClassBuilderPattern<T> cloneObject(T entity){
            this.entity = entity;
            return this;
        }

        public ClassBuilderPattern<T> mapToClass(Object object) {
            modelMapper.map(object,this.entity);
            return this;
        }

        public T build(){
            return entity;
    }
}

