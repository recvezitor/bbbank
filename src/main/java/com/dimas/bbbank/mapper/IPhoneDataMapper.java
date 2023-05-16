package com.dimas.bbbank.mapper;

import com.dimas.bbbank.api.ApiPhoneData;
import com.dimas.bbbank.domain.entity.PhoneData;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface IPhoneDataMapper {

    ApiPhoneData map(PhoneData source);

    List<ApiPhoneData> mapAsList(List<PhoneData> source);

}