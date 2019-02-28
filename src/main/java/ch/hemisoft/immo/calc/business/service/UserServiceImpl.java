package ch.hemisoft.immo.calc.business.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.commons.exception.EmailExistsException;
import ch.hemisoft.commons.exception.UserNameExistsException;
import ch.hemisoft.immo.calc.backend.repository.UserRepository;
import ch.hemisoft.immo.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    
    @Override
    public User save(User user) {
        if (emailExist(user.getEmail()))        throw new EmailExistsException(user);
        if (userNameExist(user.getUserName()))  throw new UserNameExistsException(user);
        return repository.save(user);
    }

    private boolean emailExist(String email) {
        return repository.findByEmail(email) != null ? true : false;
    }

    private boolean userNameExist(String userName) {
        return repository.findByUserName(userName) != null ? true : false;
    }
}
