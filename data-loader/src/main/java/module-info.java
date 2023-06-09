module data.loader {
    requires mysql.connector.java;
    requires org.jdbi.v3.core;
    requires moshi;
    requires evo.inflector;
    requires com.google.common;
    requires lombok;
    requires java.sql;

    exports org.dataLoader;
    exports org.dataLoader.fileMapper;
    exports org.dataLoader.databaseMapper;
    exports org.dataLoader.jsonMapper;
}

/*
requires org.controlsfx.controls;

        groupId        controls to artifactID
 */