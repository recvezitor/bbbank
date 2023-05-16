package com.dimas.bbbank.domain;

import com.dimas.bbbank.domain.entity.PhoneData;
import com.dimas.bbbank.domain.entity.QPhoneData;
import com.dimas.bbbank.domain.entity.QUser;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDate;

import static java.util.Objects.isNull;


@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
public class UserFilter implements IEntityFilter {

    private String name;
    private String phone;
    private String email;
    private LocalDate dateOfBirth;

    @Override
    public Predicate toPredicate() {
        BooleanExpression predicate = QUser.user.id.isNotNull();
        if (!isNull(name)) {
            predicate = predicate.and(QUser.user.name.like("%" + name));
        }
        if (!isNull(dateOfBirth)) {
            predicate = predicate.and(QUser.user.dateOfBirth.before(dateOfBirth));
        }
//        if (!isNull(phone)) {
//            Expression<PhoneData> ex = (Expression)QPhoneData.phoneData.phone.eq(phone);
//            predicate = predicate.and(QUser.user.phoneData.contains(ex));
//        }
        return predicate;
    }


}
