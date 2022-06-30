import data.config.DataConfig;
import data.model.User;
import data.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataConfig.class)
public class DataTest {
    @Autowired
    UserService userService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void userGetTest() {
        Assert.assertNull(userService.getUser(0L));
    }

    @Test
    public void userAddTest() {
        Long id = userService.addUser(createRandomUser());
        Assert.assertNotNull(userService.getUser(id));
    }

    @Test
    public void userRemoveTest() {
        Long id = userService.addUser(createRandomUser());
        userService.removeUser(id);
        Assert.assertNull(userService.getUser(id));
    }

    @Test
    public void userUpdateTest() {
        Long id = userService.addUser(createRandomUser());
        User user = userService.getUser(id);
        String newName = RandomString.make(8);
        user.setFirstName(newName);
        userService.updateUser(user);
        Assert.assertEquals(newName, userService.getUser(id).getFirstName());
    }

    @Test
    public void userClearTest() {
        Long id = userService.addUser(createRandomUser());
        userService.clearUsers();
        Assert.assertNull(userService.getUser(id));
    }

    @Test
    public void userListTest() {
        userService.clearUsers();
        userService.addUser(createRandomUser());
        userService.addUser(createRandomUser());
        Assert.assertEquals(2, userService.listUsers().size());
    }

    private User createRandomUser() {
        return new User(RandomString.make(8), RandomString.make(8));
    }

    @Test
    public void messageTest() {
         Assert.assertEquals("Тестовое сообщение",
                 applicationContext.getMessage("testmessage", null, Locale.forLanguageTag("ru")));
    }
}
