package cc.akkaha.shop.controllers.model;

public class StatisticsQuery {

    private String start;
    private String end;
    private Integer user;

    private boolean byLevel;
    private boolean byWeight;
    private boolean isHome;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public boolean getByLevel() {
        return byLevel;
    }

    public void setByLevel(boolean byLevel) {
        this.byLevel = byLevel;
    }

    public boolean getByWeight() {
        return byWeight;
    }

    public void setByWeight(boolean byWeight) {
        this.byWeight = byWeight;
    }

    public boolean getIsHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }
}
