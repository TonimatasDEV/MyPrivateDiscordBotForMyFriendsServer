package dev.tonimatas.config.data;

import dev.tonimatas.config.JsonFile;

public class ExtraData extends JsonFile {
    private long count;

    @SuppressWarnings("unused")
    public ExtraData() {
        this(0);
    }

    public ExtraData(long count) {
        this.count = count;
    }

    @Override
    protected String getFilePath() {
        return "data/extra.json";
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
        save();
    }
}
