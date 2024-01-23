package br.com.alura.screenmach.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> tClass);
}
