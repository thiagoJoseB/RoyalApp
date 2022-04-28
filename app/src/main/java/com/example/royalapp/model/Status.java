package com.example.royalapp.model;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author suporte
 */
public enum Status {
    OK(0),
    EMAIL_REPETIDO(1062),
    REQUISICAO_INVALIDA(69)
    ;
    
    private Status(int codigo){
	this.codigo = codigo;
    }
    
    public static Status porCodigo(int codigo){
	return Stream.of(Status.values()).filter(status -> status.codigo == codigo).findAny().orElse(null);
    }
    
    public final int codigo;
    
    
}
