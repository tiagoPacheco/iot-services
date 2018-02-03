//Este código veio do GIT github.com/nglauber
/*
Copyright (c) 2018 Nglauber - github.com/nglauber
        All rights reserved.


*/
package br.com.dts.services.service;

public interface Mp3Service {
    void play(String arquivo);
    void pause();
    void stop();
    String getMusicaAtual();
    int getTempoTotal();
    int getTempoDecorrido();
}
