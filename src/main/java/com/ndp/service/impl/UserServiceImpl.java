package com.ndp.service.impl;

import com.ndp.exception.BadRequestException;
import com.ndp.exception.NotFoundException;
import com.ndp.exception.ServiceException;
import com.ndp.model.dto.request.RegisterUserDto;
import com.ndp.model.dto.request.SearchUserDto;
import com.ndp.model.dto.request.UpdatePassUserDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.dto.response.UserResponseDto;
import com.ndp.model.entity.Role;
import com.ndp.model.entity.User;
import com.ndp.repository.RoleRepository;
import com.ndp.repository.UserRepository;
import com.ndp.repository.dao.UserDao;
import com.ndp.service.UserService;
import com.ndp.util.AccountUtil;
import com.ndp.util.EmailUtil;
import com.ndp.util.EncryptUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${app.default-password}")
    private String defaultPassword;

    private static final String SUCCESS = "Success";

    @Override
    public ResponseDto registerUser(RegisterUserDto dto) {
        if (StringUtils.isEmpty(dto.getUsername())) {
            throw new BadRequestException("Username cannot be null or empty");
        }

        if (StringUtils.isEmpty(dto.getName())) {
            throw new BadRequestException("Name cannot be null or empty");
        }

        if (StringUtils.isNotEmpty(dto.getEmail()) && !EmailUtil.isValidEmail(dto.getEmail())) {
            throw new BadRequestException("Invalid email format");
        }

        if (dto.getRoleId() == null) {
            throw new BadRequestException("Role ID cannot be null or empty");
        }

        dto.setEmail(StringUtils.isEmpty(dto.getEmail()) ? "-" : dto.getEmail());
        Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (role == null) {
            throw new NotFoundException("Role detail not found");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setMustChangePassword(true);
        user.setRole(role);
        user.setPassword(EncryptUtil.encrypt(defaultPassword));
        user = userRepository.save(user);

        return new ResponseDto(201, SUCCESS, user);
    }

    @Override
    public ResponseDto resetPass(UpdatePassUserDto dto) {
        if (dto.getUserId() == null) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        User user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User detail not found");
        }

        user.setPassword(EncryptUtil.encrypt(defaultPassword));
        user.setMustChangePassword(true);
        user = userRepository.save(user);

        return new ResponseDto(200, SUCCESS, user);
    }

    @Override
    public ResponseDto updatePass(UpdatePassUserDto dto) {
        if (dto.getUserId() == null) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        if (StringUtils.isEmpty(dto.getOldPassword())) {
            throw new BadRequestException("Old password cannot be null or empty");
        }

        if (StringUtils.isEmpty(dto.getNewPassword())) {
            throw new BadRequestException("New password cannot be null or empty");
        }

        if (dto.getOldPassword().equals(dto.getNewPassword())) {
            throw new BadRequestException("New password cannot be same than old password");
        }

        User user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User detail not found");
        }

        if (!Objects.equals(EncryptUtil.encrypt(dto.getOldPassword()), user.getPassword())) {
            throw new BadRequestException("Invalid old password");
        }

        user.setPassword(EncryptUtil.encrypt(dto.getNewPassword()));
        user.setMustChangePassword(false);
        user = userRepository.save(user);

        return new ResponseDto(200, SUCCESS, user);
    }

    @Override
    public ResponseDto deleteUser(UpdatePassUserDto dto) {
        if (dto.getUserId() == null) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        User user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User detail not found");
        }

        user.setDeleted(true);
        userRepository.save(user);

        return new ResponseDto(200, SUCCESS, null);
    }

    @Override
    public ResponseDto statusUpdate(UpdatePassUserDto dto, boolean isActive) {
        if (dto.getUserId() == null) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        User user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            throw new NotFoundException("User detail not found");
        }

        user.setActive(!isActive);
        userRepository.save(user);

        return new ResponseDto(200, SUCCESS, null);
    }

    @Override
    public PageResponseDto searchUser(SearchUserDto dto) {
        try {
            UserDao userDao = new UserDao();
            List<UserResponseDto> results = new ArrayList<>();

            Page<User> users = userRepository.findAll(
                    userDao.buildFindUsers(dto),
                    PageRequest.of(
                            dto.getPage() == null ? 0 : dto.getPage(),
                            dto.getSize() == null || dto.getSize() <= 0 ? 1 : dto.getSize(),
                            Sort.by(
                                    dto.getSort() == null ? Sort.Direction.ASC : dto.getSort(),
                                    StringUtils.isEmpty(dto.getSortBy()) ? "name" : dto.getSortBy()
                            )
                    )
            );

            users.forEach(x -> results.add(new UserResponseDto(
                    x.getId(),
                    x.getUsername(),
                    x.getName(),
                    x.getEmail(),
                    x.getRole().getName(),
                    x.getRole().getDescription(),
                    x.isActive(),
                    x.getCreatedDate(),
                    x.getUpdatedDate()
            )));

            return new PageResponseDto(200, SUCCESS, results, users.getNumber(),
                    users.getSize(), users.getTotalPages(), users.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
