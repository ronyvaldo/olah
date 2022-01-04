package com.olah.clients.model.dominio;

public enum DominioRole {

    USUARIO_COMUM,
    USUARIO_MEMBRO,
    USUARIO_ADMINISTRADOR,
    USUARIO_MASTER;

    public static String getRole(Integer perfil) {
        if (perfil == 0) {
            return USUARIO_COMUM.toString();
        } else if (perfil == 1) {
            return USUARIO_MEMBRO.toString();
        } else if (perfil == 2) {
            return USUARIO_ADMINISTRADOR.toString();
        } else if (perfil == 3) {
            return USUARIO_MASTER.toString();
        }
        return "USER";
    }

}
