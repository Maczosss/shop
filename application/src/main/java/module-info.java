module application {
    requires data.loader;
    requires org.jdbi.v3.core;
    requires lombok;
    requires java.sql;

    exports org.application;
    exports org.application.api;
    exports org.application.model;
}
