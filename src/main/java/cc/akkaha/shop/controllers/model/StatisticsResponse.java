package cc.akkaha.shop.controllers.model;

import java.util.List;
import java.util.Map;

public class StatisticsResponse {

    private List<Map<String, Object>> byLevel;
    private List<Map<String, Object>> byWeight;
    private List<Map<String, Object>> byDate;

    public List<Map<String, Object>> getByLevel() {
        return byLevel;
    }

    public void setByLevel(List<Map<String, Object>> byLevel) {
        this.byLevel = byLevel;
    }

    public List<Map<String, Object>> getByWeight() {
        return byWeight;
    }

    public void setByWeight(List<Map<String, Object>> byWeight) {
        this.byWeight = byWeight;
    }

    public List<Map<String, Object>> getByDate() {
        return byDate;
    }

    public void setByDate(List<Map<String, Object>> byDate) {
        this.byDate = byDate;
    }
}
