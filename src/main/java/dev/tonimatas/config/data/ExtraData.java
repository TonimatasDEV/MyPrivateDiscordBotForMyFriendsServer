package dev.tonimatas.config.data;

import dev.tonimatas.config.JsonFile;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtraData extends JsonFile {
    private long count;

    public final Map<String, String> reportToOriginalMap = new ConcurrentHashMap<>();

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
    }

    public Map<String, String> getReportToOriginalMap() {
        return this.reportToOriginalMap;
    }

    public void addReport() {

    }
}
