package br.com.fiap.techflix.enums;

public enum Categoria {
    ACAO,
    COMEDIA,
    DRAMA,
    FICCAO_CIENTIFICA,
    ROMANCE,
    TERROR,
    ANIMACAO,
    AVENTURA,
    DOCUMENTARIO,
    FANTASIA,
    MUSICAL,
    SUSPENSE,
    OUTRO;

    public static Categoria fromString(String categoriaStr) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.name().equalsIgnoreCase(categoriaStr)) {
                return categoria;
            }
        }
        return Categoria.OUTRO;
    }
}
