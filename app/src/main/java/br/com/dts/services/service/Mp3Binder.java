//Este código veio do GIT github.com/nglauber
/*
Copyright (c) 2018 Nglauber - github.com/nglauber
        All rights reserved.


*/
package br.com.dts.services.service;

import android.os.Binder;

public class Mp3Binder extends Binder {
    private Mp3Service mServico;
    public Mp3Binder(Mp3Service s) {
        mServico = s;
    }
    public Mp3Service getServico() {
        return mServico;
    }
}
