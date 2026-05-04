package cz.miou.h2.mariadb.spatial;

import cz.miou.h2.api.ExtensionLoader;

public class H2GisLoader implements ExtensionLoader {

    @Override
    public String getName() {
        return "H2GIS_SPATIAL";
    }

    @Override
    public String getClassName() {
        return "org.h2gis.functions.factory.H2GISFunctions";
    }

    @Override
    public String getMethodName() {
        return "load";
    }

}
