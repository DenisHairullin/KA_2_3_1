import data.config.DataConfig;
import data.model.User;
import data.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
public class DataTest {
    @Autowired
    UserService userService;

    @Test
    @Transactional
    public void userGetTest() {
        Assert.assertNull(userService.getUser(0L));
    }

    @Test
    @Transactional
    public void userAddTest() {
        Long id = userService.addUser(createRandomUser());
        Assert.assertNotNull(userService.getUser(id));
        userService.removeUser(id);
    }

    @Test
    @Transactional
    public void userRemoveTest() {
        Long id = userService.addUser(createRandomUser());
        userService.removeUser(id);
        Assert.assertNull(userService.getUser(id));
    }

    @Test
    @Transactional
    public void userUpdateTest() {
        Long id = userService.addUser(createRandomUser());
        User user = userService.getUser(id);
        String newName = RandomString.make(8);
        user.setFirstName(newName);
        userService.updateUser(user);
        Assert.assertEquals(userService.getUser(id).getFirstName(), newName);
        userService.removeUser(id);
    }

    @Test
    @Transactional
    public void userClearTest() {
        Long id = userService.addUser(createRandomUser());
        userService.clearUsers();
        Assert.assertNull(userService.getUser(id));
    }

    @Test
    @Transactional
    public void userListTest() {
        userService.clearUsers();
        userService.addUser(createRandomUser());
        userService.addUser(createRandomUser());
        Assert.assertEquals(userService.listUsers().size(), 2);
    }

    private User createRandomUser() {
        return new User(RandomString.make(8), RandomString.make(8));
    }
}
