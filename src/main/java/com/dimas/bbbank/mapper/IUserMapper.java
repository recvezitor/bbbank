package com.dimas.bbbank.mapper;

import com.dimas.bbbank.api.ApiUser;
import com.dimas.bbbank.api.ApiUserFilterRequest;
import com.dimas.bbbank.domain.UserFilter;
import com.dimas.bbbank.domain.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {IAccountMapper.class, IEmailDataMapper.class, IPhoneDataMapper.class})
public interface IUserMapper {

    ApiUser map(User source);

    List<ApiUser> mapAsList(List<User> source);

    UserFilter map(ApiUserFilterRequest source);

}