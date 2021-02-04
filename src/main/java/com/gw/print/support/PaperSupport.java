package com.gw.print.support;

import javafx.print.Paper;

/**
 * Created by ggs.
 */
public class PaperSupport {
    public static double getPaperWidth(int paper){
        return findPaperMeta(paper).getWidth();
    }

    public static double getPaperHeight(int paper){
        return findPaperMeta(paper).getHeight();
    }

    private static Paper findPaperMeta(int paper){
        switch (paper){
            case 4:
                return Paper.A4;
            case 3:
                return Paper.A3;
            case 5:
                return Paper.A5;
            case 39:
                return Paper.NA_LETTER;
            case 0:
                return Paper.A0;
            case 1:
                return Paper.A1;
            case 2:
                return Paper.A2;
            case 6:
                return Paper.A6;
            case 15:
                return Paper.JIS_B4;
            case 16:
                return Paper.JIS_B5;
            case 17:
                return Paper.JIS_B6;

        }
        return Paper.A4;
    }
}
