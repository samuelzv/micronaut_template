package server.account.repository;

import com.org.mmo_server.repository.model.tables.daos.UserRolesDao;
import com.org.mmo_server.repository.model.tables.daos.UsersDao;
import com.org.mmo_server.repository.model.tables.pojos.UserRoles;
import com.org.mmo_server.repository.model.tables.pojos.Users;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.org.mmo_server.repository.model.tables.Users.*;

@Singleton
public class AccountRepository {
    // Use DSL context for higher performance results
    @Inject
    DSLContext dslContext;

    UsersDao usersDao;
    UserRolesDao userRolesDao;

    AccountRepository(Configuration configuration) {
        this.usersDao = new UsersDao(configuration);
        this.userRolesDao = new UserRolesDao(configuration);
    }

    public Users fetchByUsername(String username) {
        return usersDao.fetchOneByUsername(username);
    }

    public boolean validCredentials(String username, String password) {
        Users user = dslContext.selectFrom(USERS)
                .where(USERS.USERNAME.equal(username).and(USERS.PASSWORD.equal(password)))
                .fetchAnyInto(Users.class);

        return null != user;
    }

    public List<String> getRolesForUser(String username) {
        return userRolesDao.fetchByUsername(username)
                .stream()
                .map(UserRoles::getRole)
                .collect(Collectors.toList());
    }
}
