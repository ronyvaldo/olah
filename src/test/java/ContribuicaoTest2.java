import com.olah.clients.ClientApplications;
import com.olah.clients.model.entity.Contribuicao;
import com.olah.clients.model.entity.TipoContribuicao;
import com.olah.clients.model.repository.ContribuicaoRepository;
import com.olah.clients.model.repository.UsuarioRepository;
import com.olah.clients.rest.ContribuicaoController;
import com.olah.clients.rest.UsuarioController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(classes = ClientApplications.class)
public class ContribuicaoTest2 {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContribuicaoRepository repository;

    @Test
    public void findAll() throws Exception {
        List<Contribuicao> contribuicoes = new ArrayList<>();
        when(repository.findAll()).thenReturn(contribuicoes);
        this.mockMvc.perform(get("/api/contribuicoes"))
                        .andExpect(status().isOk())
                .andExpect(content().string(containsString("teste")));
        /*Contribuicao contribuicao = new Contribuicao();
        TipoContribuicao tipoContribuicao = new TipoContribuicao();
        tipoContribuicao.setId(1);
        contribuicao.setTipoContribuicao(tipoContribuicao);
        contribuicao.setDataContribuicao(new Date());*/
    }
}
