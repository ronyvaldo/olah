package com.olah.clients.model.dominio;

public enum DominioTipoRedeSocial {

    GOOGLE(1, "Google"),
    Facebook(2, "Facebook");

    private Integer codigo;
    private String descricao;

    DominioTipoRedeSocial(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static String getDescricaoRedeSocial(Integer codigo) {
        for (DominioTipoRedeSocial value : DominioTipoRedeSocial.values()) {
            if (value.ordinal() == codigo) {
                return value.getDescricao();
            }
        }
        return null;
    }
}
