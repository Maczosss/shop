module application {
    requires data.loader;
    requires org.jdbi.v3.core;
    requires lombok;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens org.application.model to moshi, data.loader;

    exports org.application;
    exports org.application.api;
    exports org.application.model;
}
