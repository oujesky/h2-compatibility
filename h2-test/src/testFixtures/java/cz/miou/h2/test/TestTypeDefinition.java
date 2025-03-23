package cz.miou.h2.test;

import cz.miou.h2.api.TypeDefinition;

public class TestTypeDefinition implements TypeDefinition {

    @Override
    public String getName() {
        return "TEST_TYPE";
    }

    @Override
    public String getType() {
        return "INT";
    }
}
