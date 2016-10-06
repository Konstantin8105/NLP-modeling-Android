package com.modelingbrain.home.opensave;

@SuppressWarnings("SpellCheckingInspection")
public class ValuesIO {
    public static final String OUTPUT_FILENAME_JSON = "NlpModeling.json";

    public enum formats{
        JSON (".json"),
        XML(".xml");

        private final String format;

        formats(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }
    }

    public static class JsonElemenets{
        public static final String TYPE = "type";
        public static final String NAME = "name";
        public static final String TIME = "time";
        public static final String RIGHT = "right";
    }
}
