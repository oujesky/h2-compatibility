package cz.miou.h2.mariadb.hash;

import cz.miou.h2.api.FunctionAlias;

public class Sha implements FunctionAlias {

    @Override
    public String getName() {
        return "SHA";
    }

    @Override
    public String getClassName() {
        return "cz.miou.h2.mariadb.hash.Sha1";
    }

    @Override
    public String getMethodName() {
        return "sha1";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
}
