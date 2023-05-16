package com.dimas.bbbank.mapper;

import com.dimas.bbbank.api.ApiEmailData;
import com.dimas.bbbank.domain.entity.EmailData;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface IEmailDataMapper {

    ApiEmailData map(EmailData source);

    List<ApiEmailData> mapAsList(List<EmailData> source);

}