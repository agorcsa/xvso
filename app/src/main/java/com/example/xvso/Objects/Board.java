package com.example.xvso.Objects;

import java.util.ArrayList;

public class Board {

    private ArrayList<Cell> cellArrayList = new ArrayList<>(9);

    public void addCell() {
        for (int i = 0; i < 9; i++) {
            cellArrayList.add(new Cell());
        }
    }
}
