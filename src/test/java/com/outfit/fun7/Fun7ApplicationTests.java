package com.outfit.fun7;

import com.outfit.fun7.api.MainController;
import com.outfit.fun7.model.User;
import com.outfit.fun7.repository.UserRepository;
import com.outfit.fun7.service.FeaturesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.hamcrest.core.Is;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest()
class Fun7ApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void userWithHighUsageAndFromUS_hasMultiplayerEnabled() throws Exception {
		String user_id = "5634161670881280";
		String timezone = "America/New_York";
		String countryCode = "US";

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/features?userId=" + user_id + "&timezone=" + timezone + "&cc=" + countryCode)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("multiplayer", Is.is("enabled")));
	}

	@Test
	void userWithHighUsageAndFromSI_hasMultiplayerDisabled() throws Exception {
		String user_id = "5634161670881280";
		String timezone = "Europe/Ljubljana";
		String countryCode = "SI";

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/features?userId=" + user_id + "&timezone=" + timezone + "&cc=" + countryCode)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("multiplayer", Is.is("disabled")));
	}

	@Test
	void userWithLowUsageAndFromUS_hasMultiplayerDisabled() throws Exception {
		String user_id = "5644004762845184";
		String timezone = "America/New_York";
		String countryCode = "US";

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/features?userId=" + user_id + "&timezone=" + timezone + "&cc=" + countryCode)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("multiplayer", Is.is("disabled")));
	}

	@Test
	void userFromSI_hasCustomerSupportDisabled_afterWorkHours() throws Exception {
		String user_id = "5634161670881280";
		String timezone = "Europe/Ljubljana";
		String countryCode = "SI";

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/features?userId=" + user_id + "&timezone=" + timezone + "&cc=" + countryCode)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("user-support", Is.is("disabled")));
	}

	@Test
	void userFromSI_hasCustomerSupportEnabled_inWorkHours() throws Exception {
		String user_id = "5634161670881280";
		String timezone = "Europe/Ljubljana";
		String countryCode = "SI";

		mvc.perform(MockMvcRequestBuilders.get("/api/v1/features?userId=" + user_id + "&timezone=" + timezone + "&cc=" + countryCode)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("user-support", Is.is("enabled")));
	}

}
