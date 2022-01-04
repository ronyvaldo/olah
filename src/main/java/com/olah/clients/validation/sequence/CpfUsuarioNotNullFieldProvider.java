package com.olah.clients.validation.sequence;

import com.olah.clients.model.entity.Usuario;
import com.olah.clients.validation.groups.NotNullField;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class CpfUsuarioNotNullFieldProvider implements DefaultGroupSequenceProvider<Usuario> {

    public List<Class<?>> getValidationGroups(Usuario usuario) {
        List<Class<?>> groups = new ArrayList<>();

        /*
         * Informamos ao HibernateValidator para usar as validações default
         * definidas na classe Cliente.
         */
        groups.add(Usuario.class);

        if (usuario != null) {
            /*
             * Aqui nós implementamos a lógica que determina o grupo de
             * validação a ser aplicado ao bean.
             */
            if (usuario.getCpf() != null && !usuario.getCpf().isEmpty()) {
                groups.add(NotNullField.class);
            }
        }
        return groups;
    }
}
