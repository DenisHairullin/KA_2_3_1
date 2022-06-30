package crud;

import crud.data.model.User;
import crud.data.repository.UserRepository;
import crud.data.service.UserService;
import crud.data.service.UserServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserServiceImpl.class)
public class DataTest {
    @Autowired
    UserRepository userCrudRepository;

    @Autowired
    UserService userService;

    @Autowired
    DataSource dataSource;

    @Test
    public void userGetTest() {
        Assert.assertEquals(Optional.empty(), userCrudRepository.findById(0L));
    }

    @Test
    public void userAddTest() {
        Long id = userService.save(createRandomUser()).getId();
        Assert.assertNotNull(userService.findById(id));
    }

    @Test
    public void userRemoveTest() {
        Long id = userService.save(createRandomUser()).getId();
        userService.deleteById(id);
        Assert.assertEquals(Optional.empty(), userService.findById(id));
    }

    @Test
    public void userUpdateTest() {
        Long id = userService.save(createRandomUser()).getId();
        User user = userService.findById(id).get();
        String newName = RandomString.make(8);
        user.setFirstName(newName);
        userService.save(user);
        Assert.assertEquals(newName, userService.findById(id).get().getFirstName());
    }

    @Test
    public void userClearTest() {
        Long id = userService.save(createRandomUser()).getId();
        userService.deleteAll();
        Assert.assertEquals(Optional.empty(), userService.findById(id));
    }

    @Test
    public void userListTest() {
        userService.deleteAll();
        userService.save(createRandomUser());
        userService.save(createRandomUser());
        Assert.assertEquals(2, StreamSupport.stream(userService.findAll().spliterator(), false)
                .count());
    }

    private User createRandomUser() {
        return new User(RandomString.make(8), RandomString.make(8));
    }
}
