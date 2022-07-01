package crud;

import crud.service.UserService;
import crud.controller.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void usersViewTest() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    public void userFormViewTest() throws Exception {
        mockMvc.perform(get("/users/add")).andExpect(status().isOk());
    }

    @Test
    public void userNotFoundViewTest() throws Exception {
        mockMvc.perform(get("/users/remove?id=0")).andExpect(status().isOk());
    }
}
