package com.amalitech.usermanagementservice.config.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}


//public ModelMapper mapper(){
//    ModelMapper modelMapper = new ModelMapper();
//    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//    // Handle Hibernate PersistentBag
//    modelMapper.createTypeMap(org.hibernate.collection.spi.PersistentBag.class, List.class)
//            .setConverter(ctx -> new ArrayList<>(ctx.getSource()));
//
//    return modelMapper;
//}