package com.mkalugin;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableHexEntry {
    private final SimpleStringProperty c0;
    private final SimpleStringProperty c1;
    private final SimpleStringProperty c2;
    private final SimpleStringProperty c3;
    private final SimpleStringProperty c4;
    private final SimpleStringProperty c5;
    private final SimpleStringProperty c6;
    private final SimpleStringProperty c7;
    private final SimpleStringProperty c8;
    private final SimpleStringProperty c9;
    private final SimpleStringProperty ca;
    private final SimpleStringProperty cb;
    private final SimpleStringProperty cc;
    private final SimpleStringProperty cd;
    private final SimpleStringProperty ce;
    private final SimpleStringProperty cf;

    public TableHexEntry() 
    {
        this.c0 = new SimpleStringProperty("??");
        this.c1 = new SimpleStringProperty("??");
        this.c2 = new SimpleStringProperty("??");
        this.c3 = new SimpleStringProperty("??");
        this.c4 = new SimpleStringProperty("??");
        this.c5 = new SimpleStringProperty("??");
        this.c6 = new SimpleStringProperty("??");
        this.c7 = new SimpleStringProperty("??");
        this.c8 = new SimpleStringProperty("??");
        this.c9 = new SimpleStringProperty("??");
        this.ca = new SimpleStringProperty("??");
        this.cb = new SimpleStringProperty("??");
        this.cc = new SimpleStringProperty("??");
        this.cd = new SimpleStringProperty("??");
        this.ce = new SimpleStringProperty("??");
        this.cf = new SimpleStringProperty("??");
    }

    public TableHexEntry(int c0, int c1, int c2, int c3, int c4, int c5, int c6, int c7,
                         int c8, int c9, int ca, int cb, int cc, int cd, int ce, int cf) 
                        {
                            this.c0 = new SimpleStringProperty(String.format("%02X", c0));
                            this.c1 = new SimpleStringProperty(String.format("%02X", c1));
                            this.c2 = new SimpleStringProperty(String.format("%02X", c2));
                            this.c3 = new SimpleStringProperty(String.format("%02X", c3));
                            this.c4 = new SimpleStringProperty(String.format("%02X", c4));
                            this.c5 = new SimpleStringProperty(String.format("%02X", c5));
                            this.c6 = new SimpleStringProperty(String.format("%02X", c6));
                            this.c7 = new SimpleStringProperty(String.format("%02X", c7));
                            this.c8 = new SimpleStringProperty(String.format("%02X", c8));
                            this.c9 = new SimpleStringProperty(String.format("%02X", c9));
                            this.ca = new SimpleStringProperty(String.format("%02X", ca));
                            this.cb = new SimpleStringProperty(String.format("%02X", cb));
                            this.cc = new SimpleStringProperty(String.format("%02X", cc));
                            this.cd = new SimpleStringProperty(String.format("%02X", cd));
                            this.ce = new SimpleStringProperty(String.format("%02X", ce));
                            this.cf = new SimpleStringProperty(String.format("%02X", cf));                            
                        }
    
    public TableHexEntry(ArrayList<Integer> data)
    {
        if (data.size() > 0) {
            this.c0 = new SimpleStringProperty(String.format("%02X", data.get(0)));
        } else {
            this.c0 = new SimpleStringProperty("??");
        }

        if (data.size() > 1) {
            this.c1 = new SimpleStringProperty(String.format("%02X", data.get(1)));
        } else {
            this.c1 = new SimpleStringProperty("??");
        }

        if (data.size() > 2) {
            this.c2 = new SimpleStringProperty(String.format("%02X", data.get(2)));
        } else {
            this.c2 = new SimpleStringProperty("??");
        }

        if (data.size() > 3) {
            this.c3 = new SimpleStringProperty(String.format("%02X", data.get(3)));
        } else {
            this.c3 = new SimpleStringProperty("??");
        }

        if (data.size() > 4) {
            this.c4 = new SimpleStringProperty(String.format("%02X", data.get(4)));
        } else {
            this.c4 = new SimpleStringProperty("??");
        }

        if (data.size() > 5) {
            this.c5 = new SimpleStringProperty(String.format("%02X", data.get(5)));
        } else {
            this.c5 = new SimpleStringProperty("??");
        }

        if (data.size() > 6) {
            this.c6 = new SimpleStringProperty(String.format("%02X", data.get(6)));
        } else {
            this.c6 = new SimpleStringProperty("??");
        }

        if (data.size() > 7) {
            this.c7 = new SimpleStringProperty(String.format("%02X", data.get(7)));
        } else {
            this.c7 = new SimpleStringProperty("??");
        }

        if (data.size() > 8) {
            this.c8 = new SimpleStringProperty(String.format("%02X", data.get(8)));
        } else {
            this.c8 = new SimpleStringProperty("??");
        }

        if (data.size() > 9) {
            this.c9 = new SimpleStringProperty(String.format("%02X", data.get(9)));
        } else {
            this.c9 = new SimpleStringProperty("??");
        }

        if (data.size() > 10) {
            this.ca = new SimpleStringProperty(String.format("%02X", data.get(10)));
        } else {
            this.ca = new SimpleStringProperty("??");
        }

        if (data.size() > 11) {
            this.cb = new SimpleStringProperty(String.format("%02X", data.get(11)));
        } else {
            this.cb = new SimpleStringProperty("??");
        }

        if (data.size() > 12) {
            this.cc = new SimpleStringProperty(String.format("%02X", data.get(12)));
        } else {
            this.cc = new SimpleStringProperty("??");
        }

        if (data.size() > 13) {
            this.cd = new SimpleStringProperty(String.format("%02X", data.get(13)));
        } else {
            this.cd = new SimpleStringProperty("??");
        }

        if (data.size() > 14) {
            this.ce = new SimpleStringProperty(String.format("%02X", data.get(14)));
        } else {
            this.ce = new SimpleStringProperty("??");
        }

        if (data.size() > 15) {
            this.cf = new SimpleStringProperty(String.format("%02X", data.get(15)));
        } else {
            this.cf = new SimpleStringProperty("??");
        }
    }   
    
    public StringProperty getc0()
    {
        return c0;
    }

    public StringProperty getc1()
    {
        return c1;
    }

    public StringProperty getc2()
    {
        return c2;
    }

    public StringProperty getc3()
    {
        return c3;
    }

    public StringProperty getc4()
    {
        return c4;
    }

    public StringProperty getc5()
    {
        return c5;
    }

    public StringProperty getc6()
    {
        return c6;
    }

    public StringProperty getc7()
    {
        return c7;
    }

    public StringProperty getc8()
    {
        return c8;
    }

    public StringProperty getc9()
    {
        return c9;
    }

    public StringProperty getca()
    {
        return ca;
    }

    public StringProperty getcb()
    {
        return cb;
    }

    public StringProperty getcc()
    {
        return cc;
    }

    public StringProperty getcd()
    {
        return cd;
    }

    public StringProperty getce()
    {
        return ce;
    }

    public StringProperty getcf()
    {
        return cf;
    }

    public StringProperty getCell(int cell)
    {
        switch(cell)
        {
            case 0: return c0;
            case 1: return c1;
            case 2: return c2;
            case 3: return c3;
            case 4: return c4;
            case 5: return c5;
            case 6: return c6;
            case 7: return c7;
            case 8: return c8;
            case 9: return c9;
            case 10: return ca;
            case 11: return cb;
            case 12: return cc;
            case 13: return cd;
            case 14: return ce;
            case 15: return cf;
            default: return new SimpleStringProperty("");
        }
    }


}
