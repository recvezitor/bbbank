package com.dimas.bbbank.service;

import com.dimas.bbbank.api.ApiEmailData;
import com.dimas.bbbank.api.ApiPhoneData;
import com.dimas.bbbank.api.ApiTransferRequest;
import com.dimas.bbbank.api.ApiUser;
import com.dimas.bbbank.api.ApiUserFilterRequest;
import com.dimas.bbbank.domain.entity.EmailData;
import com.dimas.bbbank.domain.entity.PhoneData;
import com.dimas.bbbank.domain.entity.User;
import com.dimas.bbbank.mapper.IEmailDataMapper;
import com.dimas.bbbank.mapper.IPhoneDataMapper;
import com.dimas.bbbank.mapper.IUserMapper;
import com.dimas.bbbank.repository.EmailDataRepository;
import com.dimas.bbbank.repository.PhoneDataRepository;
import com.dimas.bbbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.dimas.bbbank.util.Util.CACHE_NAME_EMAILS;
import static com.dimas.bbbank.util.Util.CACHE_NAME_PHONES;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserMapper userMapper;
    private final IEmailDataMapper emailDataMapper;
    private final IPhoneDataMapper phoneDataMapper;
    private final UserRepository userRepository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final AccountService accountService;

    public Page<ApiUser> findByFilter(ApiUserFilterRequest filter, Pageable pageable) {
        return userRepository.findAll(userMapper.map(filter).toPredicate(), pageable)
                .map(userMapper::map);
    }

    public ApiUser get(Long id) {
        return userMapper.map(findOrThrow(id));
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("User not found for id=%s", id)));
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME_EMAILS, key = "#id")
    public ApiUser addEmail(Long id, String newEmail) {
        var user = findOrThrow(id);
        user.getEmailData().add(EmailData.builder().user(user).email(newEmail).build());
        return userMapper.map(userRepository.save(user));
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME_EMAILS, key = "#id")
    public ApiUser removeEmail(Long id, String newEmail) {
        var user = findOrThrow(id);
        var emailData = user.getEmailData().stream().filter(el -> newEmail.equals(el.getEmail())).findFirst().orElseThrow();
        user.getEmailData().removeIf(el -> Objects.equals(el.getId(), emailData.getId()));
        emailDataRepository.deleteById(emailData.getId());
        return userMapper.map(userRepository.save(user));
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME_PHONES, key = "#id")
    public ApiUser addPhone(Long id, String newPhone) {
        var user = findOrThrow(id);
        user.getPhoneData().add(PhoneData.builder().user(user).phone(newPhone).build());
        return userMapper.map(userRepository.save(user));
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME_PHONES, key = "#id")
    public ApiUser removePhone(Long id, String oldPhone) {
        var user = findOrThrow(id);
        var phoneData = user.getPhoneData().stream().filter(el -> oldPhone.equals(el.getPhone())).findFirst().orElseThrow();
        user.getPhoneData().removeIf(el -> Objects.equals(el.getId(), phoneData.getId()));
        phoneDataRepository.deleteById(phoneData.getId());
        return userMapper.map(userRepository.save(user));
    }

    public boolean transfer(ApiTransferRequest request) {
        Long senderId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        var sender = findOrThrow(senderId);
        var recipient = findOrThrow(request.getRecipientId());
        return accountService.doTransfer(sender.getAccount().getId(), recipient.getAccount().getId(), request.getAmount());
    }

    @Cacheable(value = CACHE_NAME_EMAILS, key = "#userId")
    public List<ApiEmailData> getEmails(Long userId) {
        return emailDataMapper.mapAsList(emailDataRepository.findByUserId(userId));
    }

    @Cacheable(value = CACHE_NAME_PHONES, key = "#userId")
    public List<ApiPhoneData> getPhones(Long userId) {
        return phoneDataMapper.mapAsList(phoneDataRepository.findByUserId(userId));
    }

}
