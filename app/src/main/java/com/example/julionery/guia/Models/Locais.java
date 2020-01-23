package com.example.julionery.guia.Models;

import java.sql.Blob;
import java.util.ArrayList;

public class Locais {
    private int id;
    private String titulo;
    private String descricao;
    private byte[] foto;
    private String localizacao;
    private Double valor;
    private String tempo_estimado;
    private String guia;
    private Double latitude;
    private Double longitude;


    public Locais() {}

    public Locais(
            int _id,
            String _titulo,
            String _descricao,
            byte[] _foto,
            String _localizacao,
            Double _valor,
            String _tempo_estimado,
            String _guia,
            Double _latitude,
            Double _longitude
    ) {
        setId(_id);
        setTitulo(_titulo);
        setDescricao(_descricao);
        setFoto(_foto);
        setLocalizacao(_localizacao);
        setValor(_valor);
        setTempo_estimado(_tempo_estimado);
        setGuia(_guia);
        setLongitude(_longitude);
        setLatitude(_latitude);
    }

    private static int lastContactId = 0;

    public static ArrayList<Locais> createContactsList(int numContacts) {
        ArrayList<Locais> languages = new ArrayList<Locais>();

        for (int i = 1; i <= numContacts; i++) {
            languages.add(new Locais(
                    ++lastContactId,
                    "Local " + lastContactId,
                    "Descrição Local " + lastContactId,
                    null,
                    "Goiás",
                    0.0,
                    "" + lastContactId,
                    "Teste " + lastContactId,
                    0.0,
                    0.0
            ));
        }

        return languages;
    }
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTempo_estimado() {
        return tempo_estimado;
    }

    public void setTempo_estimado(String tempo_estimado) {
        this.tempo_estimado = tempo_estimado;
    }

    public String getGuia() {
        return guia;
    }

    public void setGuia(String guia) {
        this.guia = guia;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
