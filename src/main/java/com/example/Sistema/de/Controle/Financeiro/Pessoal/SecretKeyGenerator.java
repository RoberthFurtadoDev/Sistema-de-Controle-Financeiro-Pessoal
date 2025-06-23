package com.example.Sistema.de.Controle.Financeiro.Pessoal;

import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretKeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); // Para HS256
        keyGen.init(256); // 256 bits
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Sua nova JWT Secret Key Base64: " + encodedKey);
    }
}