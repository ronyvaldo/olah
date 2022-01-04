import com.olah.clients.ClientApplications;
import com.olah.clients.rest.ContribuicaoController;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

import io.restassured.http.ContentType;
import javafx.application.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ClientApplications.class)
public class ContribuicaoTest {

   private final String BASE_URL = "/api/contribuicoes";

   @Autowired
   private ContribuicaoController controller;

   @BeforeEach
   public void setup() {
       standaloneSetup(this.controller);
   }

    @Test
    public void listarTodasComSucesso() throws Exception {
        given()
                .accept(ContentType.JSON)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(HttpStatus.OK.value());
    }


}
