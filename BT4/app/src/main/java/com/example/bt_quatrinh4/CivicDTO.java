package com.example.bt_quatrinh4;

public class CivicDTO {
    private String officeName;
    private String officialName;
    private int index;

    public CivicDTO(String officeName, String officialName, int index) {
        this.officeName = officeName;
        this.officialName = officialName;
        this.index = index;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
