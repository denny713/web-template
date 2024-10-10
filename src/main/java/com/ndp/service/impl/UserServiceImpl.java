package com.ndp.service.impl;

import com.ndp.exception.BadRequestException;
import com.ndp.exception.ForbiddenException;
import com.ndp.exception.NotFoundException;
import com.ndp.exception.ServiceException;
import com.ndp.model.dto.request.RegisterUserDto;
import com.ndp.model.dto.request.SearchUserDto;
import com.ndp.model.dto.request.UpdatePassUserDto;
import com.ndp.model.dto.request.UpdateUserDto;
import com.ndp.model.dto.response.PageResponseDto;
import com.ndp.model.dto.response.ResponseDto;
import com.ndp.model.dto.response.UserResponseDto;
import com.ndp.model.entity.Role;
import com.ndp.model.entity.User;
import com.ndp.repository.RoleRepository;
import com.ndp.repository.UserRepository;
import com.ndp.repository.dao.UserDao;
import com.ndp.service.UserService;
import com.ndp.token.JwtService;
import com.ndp.util.EmailUtil;
import com.ndp.util.EncryptUtil;
import com.ndp.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Value("${app.default-password}")
    private String defaultPassword;

    private static final String SUCCESS = "Success";

    @Override
    @Transactional
    public ResponseDto getProfile(HttpServletRequest request) {
        String token = TokenUtil.getToken(request);
        if (StringUtils.isEmpty(token)) {
            throw new NotFoundException("No JWT token found in request headers or cookies");
        }

        User user = getByUsername(jwtService.getUsername(token));
        return new ResponseDto(200, SUCCESS, new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().getName(),
                user.getRole().getDescription(),
                user.isActive(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        ));
    }

    @Override
    @Transactional
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

        if (dto.getRoleId() == 0) {
            throw new BadRequestException("Role ID cannot be null or empty");
        }

        if (getUserByUsername(dto.getUsername()) != null) {
            throw new ForbiddenException("User already exists");
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
    @Transactional
    public ResponseDto updateUser(UpdateUserDto dto) {
        if (dto.getUserId() == 0) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        if (StringUtils.isEmpty(dto.getUsername())) {
            throw new BadRequestException("Username cannot be null or empty");
        }

        if (StringUtils.isEmpty(dto.getName())) {
            throw new BadRequestException("Name cannot be null or empty");
        }

        if (StringUtils.isNotEmpty(dto.getEmail()) && !EmailUtil.isValidEmail(dto.getEmail())) {
            throw new BadRequestException("Invalid email format");
        }

        if (dto.getRoleId() == 0) {
            throw new BadRequestException("Role ID cannot be null or empty");
        }

        User user = getById(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(roleRepository.findById(dto.getRoleId()).orElse(null));
        user = userRepository.save(user);

        return new ResponseDto(200, SUCCESS, user);
    }

    @Override
    @Transactional
    public ResponseDto resetPass(UpdatePassUserDto dto) {
        if (dto.getUserId() == 0) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        User user = getById(dto.getUserId());
        user.setPassword(EncryptUtil.encrypt(defaultPassword));
        user.setMustChangePassword(true);
        user = userRepository.save(user);

        return new ResponseDto(200, SUCCESS, user);
    }

    @Override
    @Transactional
    public ResponseDto updatePass(UpdatePassUserDto dto) {
        if (dto.getUserId() == 0) {
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

        User user = getById(dto.getUserId());
        if (!Objects.equals(EncryptUtil.encrypt(dto.getOldPassword()), user.getPassword())) {
            throw new BadRequestException("Invalid old password");
        }

        user.setPassword(EncryptUtil.encrypt(dto.getNewPassword()));
        user.setMustChangePassword(false);
        user = userRepository.save(user);

        return new ResponseDto(200, SUCCESS, user);
    }

    @Override
    @Transactional
    public ResponseDto deleteUser(UpdatePassUserDto dto) {
        if (dto.getUserId() == 0) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        User user = getById(dto.getUserId());
        user.setDeleted(true);
        userRepository.save(user);

        return new ResponseDto(200, SUCCESS, null);
    }

    @Override
    @Transactional
    public ResponseDto statusUpdate(UpdatePassUserDto dto, boolean isActive) {
        if (dto.getUserId() == 0) {
            throw new BadRequestException("User ID cannot be null or empty");
        }

        User user = getById(dto.getUserId());
        user.setActive(!isActive);
        userRepository.save(user);

        return new ResponseDto(200, SUCCESS, null);
    }

    @Override
    @Transactional
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
                                    StringUtils.isEmpty(dto.getSortBy()) ? "id" : dto.getSortBy()
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

            return new PageResponseDto(200, SUCCESS, results,
                    dto.getDraw() == null ? 0 : dto.getDraw(),
                    users.getTotalElements(), users.getTotalElements());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    private User getById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User detail not found");
        }

        return user.get();
    }

    private User getByUsername(String username) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    private User getUserByUsername(String username) {
        UserDao userDao = new UserDao();
        return userRepository.findOne(userDao.buildFindByUsername(username)).orElse(null);
    }
}
