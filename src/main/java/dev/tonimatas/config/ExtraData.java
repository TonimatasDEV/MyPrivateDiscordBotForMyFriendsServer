package dev.tonimatas.config;

public class ExtraData extends JsonFile {
    private long count = 0;

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
