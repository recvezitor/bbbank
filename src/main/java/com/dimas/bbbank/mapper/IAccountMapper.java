package com.dimas.bbbank.mapper;

import com.dimas.bbbank.api.ApiAccount;
import com.dimas.bbbank.api.ApiEmailData;
import com.dimas.bbbank.domain.entity.Account;
import com.dimas.bbbank.domain.entity.EmailData;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface IAccountMapper {

    ApiAccount map(Account source);

    List<ApiAccount> mapAsList(List<Account> source);

}