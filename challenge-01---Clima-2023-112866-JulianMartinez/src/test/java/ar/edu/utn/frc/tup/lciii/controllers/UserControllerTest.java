package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.UserRequestDto;
import ar.edu.utn.frc.tup.lciii.dtos.UserResponseDto;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testPostUserWhenValidUserRequestDtoThenReturnUserResponseDto() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("test@test.com", "Celsius");
        UserResponseDto userResponseDto = new UserResponseDto(1L, "secret");

        when(userService.saveUser(userRequestDto)).thenReturn(userResponseDto);

        MvcResult result = mockMvc.perform(post("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = result.getResponse().getContentAsString();
        String expectedResponseBody = new ObjectMapper().writeValueAsString(userResponseDto);

        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    public void testPostUserWhenInvalidUserRequestDtoThenReturnBadRequest() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("test@test.com", "Invalid");

        when(userService.saveUser(userRequestDto)).thenReturn(null);

        mockMvc.perform(post("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }
}