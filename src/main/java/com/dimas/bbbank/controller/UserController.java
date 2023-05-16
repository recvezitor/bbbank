package com.dimas.bbbank.controller;


import com.dimas.bbbank.api.ApiEmailData;
import com.dimas.bbbank.api.ApiPhoneData;
import com.dimas.bbbank.api.ApiTransferRequest;
import com.dimas.bbbank.api.ApiTransferResponse;
import com.dimas.bbbank.api.ApiUser;
import com.dimas.bbbank.api.ApiUserFilterRequest;
import com.dimas.bbbank.service.AccountService;
import com.dimas.bbbank.service.UserService;
import com.dimas.bbbank.validation.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

import java.util.List;

import static com.dimas.bbbank.util.Util.ROOT_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT_PATH)
public class UserController {

    private final static String PATH = "/users";

    private final UserService userService;
    private final AccountService accountService;

    @GetMapping(PATH)
    public Page<ApiUser> findByFilter(
            @Validated final ApiUserFilterRequest filter,
            @PageableDefault(size = 5, sort = {"name"}, direction = Sort.Direction.ASC) final Pageable pageable
    ) {
        return userService.findByFilter(filter, pageable);
    }

    @GetMapping(PATH + "/{id}")
    public ApiUser get(@PathVariable final Long id) {
        return userService.get(id);
    }

    @PreAuthorize("principal == #id")
    @PostMapping(value = PATH + "/{id}/emails/{newEmail}", consumes = APPLICATION_JSON_VALUE)
    public ApiUser addEmail(
            @PathVariable final Long id,
            @PathVariable @Email final String newEmail
    ) {
        return userService.addEmail(id, newEmail);
    }

    @PreAuthorize("principal == #id")
    @DeleteMapping(value = PATH + "/{id}/emails/{oldEmail}", consumes = APPLICATION_JSON_VALUE)
    public ApiUser removeEmail(
            @PathVariable final Long id,
            @PathVariable @Email final String oldEmail
    ) {
        return userService.removeEmail(id, oldEmail);
    }

    @GetMapping(PATH + "/{id}/emails")
    public List<ApiEmailData> getEmails(@PathVariable final Long id) {
        return userService.getEmails(id);
    }

    @PreAuthorize("principal == #id")
    @PostMapping(value = PATH + "/{id}/phones/{newPhone}", consumes = APPLICATION_JSON_VALUE)
    public ApiUser addPhone(
            @PathVariable final Long id,
            @PathVariable @PhoneNumber final String newPhone
    ) {
        return userService.addPhone(id, newPhone);
    }


    @PreAuthorize("principal == #id")
    @DeleteMapping(value = PATH + "/{id}/phones/{oldPhone}", consumes = APPLICATION_JSON_VALUE)
    public ApiUser removePhone(
            @PathVariable final Long id,
            @PathVariable @PhoneNumber final String oldPhone
    ) {
        return userService.removePhone(id, oldPhone);
    }

    @GetMapping(PATH + "/{id}/phones")
    public List<ApiPhoneData> getPhones(@PathVariable final Long id) {
        return userService.getPhones(id);
    }

    @PostMapping(value = PATH + "/transfer", consumes = APPLICATION_JSON_VALUE)
    public ApiTransferResponse transfer(@RequestBody final ApiTransferRequest request) {
        return ApiTransferResponse.builder().success(userService.transfer(request)).build();
    }

    //TODO remove
    @PostMapping(value = PATH + "/{id}/auto-popup", consumes = APPLICATION_JSON_VALUE)
    public void popup(@PathVariable final Long id) {
        accountService.autoPopup(id);
    }

}
