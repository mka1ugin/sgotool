package com.mkalugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Sgotool {

    static File sourcefile;
    static RandomAccessFile ram;

    static byte[] rawdata;
    static int currentOffset = 0;
   
    static Datablock[] db;

    static byte[] sgoCompatibilityArray = new byte[4];
    static int sgoCompatibility;
    static byte[] sgoChecksumArray = new byte[4];
    static int sgoChecksum;

    static byte[] identsOffsetArray = new byte[4];
    static int identsOffset;
    static byte[] baudOffsetArray = new byte[4];
    static int baudOffset;
    static byte[] reiOffsetArray = new byte[4];
    static int reiOffset;
    static byte[] acpOffsetArray = new byte[4];
    static int acpOffset;
    static byte[] sa2OffsetArray = new byte[4];
    static int sa2Offset;
    static byte[] datablocksOffsetArray = new byte[4];
    static int datablocksOffset;

    static byte[] datablocksCounterArray = new byte[4];
    static int datablocksCounter;

    static String identificationData;
    static byte headerByte[] = {0x53, 0x47, 0x4d, 0x4c, 0x20, 0x4f, 0x62, 0x6a,
                                0x65, 0x63, 0x74, 0x20, 0x46, 0x69, 0x6c, 0x65};
 
    public static void showAlert(String alertText) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("");
        alert.setContentText(alertText);
        alert.show();        
    }

    public static boolean openFile(File file) throws IOException {

        Ui.data.clear();
        Ui.blocknumbers.clear();

        sourcefile = file;
  
        ram = new RandomAccessFile(sourcefile, "r");

        if (ram.length() < 1) {
            showAlert("Файл пустой!");
            ram.close();
            return false;
        } 
        else
        {
            rawdata = new byte[(int)ram.length()];
            ram.read(rawdata);
            ram.close();
            return parseData();            
        }
    }

    public static void saveFile(File file) throws IOException {
        RandomAccessFile outFile = new RandomAccessFile(file, "rw");
        outFile.write(rawdata);
        outFile.close();
    }

    public static boolean checkSgoHeader() {
        for (int i = 0; i < headerByte.length; i++) {
            if (rawdata[i] != headerByte[i]) {
                showAlert("Некорректный файл!");
                return false;
            }
        }
        return true;
    }

    public static boolean parseData() throws IOException {

        Ui.data.clear();
        
        if (!checkSgoHeader()) {
            return false;
        }

        Ui.data.add(new TableEntry("Размер файла SGO", String.format("%d байт", rawdata.length), ""));

        currentOffset = 17;

        System.arraycopy(rawdata, currentOffset, sgoCompatibilityArray, 0, 4);
        currentOffset += 4;
        System.arraycopy(rawdata, currentOffset, sgoChecksumArray, 0, 4);
        currentOffset += 4;
        System.arraycopy(rawdata, currentOffset, identsOffsetArray, 0, 4);
        currentOffset += 4;
        System.arraycopy(rawdata, currentOffset, baudOffsetArray, 0, 4);
        currentOffset += 4;
        System.arraycopy(rawdata, currentOffset, reiOffsetArray, 0, 4);
        currentOffset += 4;
        System.arraycopy(rawdata, currentOffset, acpOffsetArray, 0, 4);
        currentOffset += 4;
        System.arraycopy(rawdata, currentOffset, sa2OffsetArray, 0, 4);
        currentOffset += 4;
        System.arraycopy(rawdata, currentOffset, datablocksOffsetArray, 0, 4);

        sgoCompatibility = fourBytesToInt(sgoCompatibilityArray);
        System.out.format("SGO compatibility: 0x%X \r\n", sgoCompatibility);

        sgoChecksum = fourBytesToInt(sgoChecksumArray);
        Ui.data.add(new TableEntry("Контрольная сумма файла", String.format("0x%X", sgoChecksum), String.format("0x%08X", 0x15)));

        identsOffset = fourBytesToInt(identsOffsetArray);
        System.out.format("SGO idents offset: 0x%X \r\n", identsOffset);

        baudOffset = fourBytesToInt(baudOffsetArray);
        System.out.format("SGO baudrate offset: 0x%X \r\n", baudOffset);

        reiOffset = fourBytesToInt(reiOffsetArray);
        System.out.format("SGO REI offset: 0x%X \r\n", reiOffset);

        acpOffset = fourBytesToInt(acpOffsetArray);
        System.out.format("SGO ACP offset: 0x%X \r\n", acpOffset);

        sa2Offset = fourBytesToInt(sa2OffsetArray);
        System.out.format("SGO SA2 offset: 0x%X \r\n", sa2Offset);

        datablocksOffset = fourBytesToInt(datablocksOffsetArray);
        System.out.format("SGO datablocks offset: 0x%X \r\n", datablocksOffset);
        
        readIdents();

        readSA2();

        readDatablocksData();

        return true;
    }

    public static void readIdents() throws IOException
    {
        identificationData = "";
        currentOffset = identsOffset;
        byte b = rawdata[currentOffset];
        while (b != 0)
        {
            identificationData += Character.toString(b ^ 0xFFFFFFFF);
            
            currentOffset++;
            b = rawdata[currentOffset];
        }
        Ui.data.add(new TableEntry("Идентификатор файла", identificationData, String.format("0x%08X", identsOffset)));
    }

    public static void readSA2() throws IOException
    {
        currentOffset = sa2Offset;
        byte[] temp = new byte[4];
        System.arraycopy(rawdata, currentOffset, temp, 0, 4);
        currentOffset += 4;
        int sa2bytescounter = fourBytesToInt(temp);
        System.out.format("SA2 len: %d \r\n", sa2bytescounter); 

        String sa2String = "";
        for (int i = 0; i < sa2bytescounter; i++)
        {
            sa2String += String.format("%02X ", rawdata[currentOffset]);
            currentOffset++;
        }
        Ui.data.add(new TableEntry("SA2", sa2String, String.format("0x%08X", sa2Offset + 4)));
    }
    
    public static void readDatablocksData() throws IOException
    {
        currentOffset = datablocksOffset;
        System.arraycopy(rawdata, currentOffset, datablocksCounterArray, 0, 4);
        currentOffset += 4;

        datablocksCounter = fourBytesToInt(datablocksCounterArray);
        Ui.data.add(new TableEntry("Количество блоков данных", String.format("%X", datablocksCounter), String.format("0x%08X", datablocksOffset)));

        db = new Datablock[datablocksCounter];

        int[] datablocksAddressesArray = new int[datablocksCounter];
        for (int i = 0; i < datablocksCounter; i++)
        {
            if (!Ui.blocknumbers.contains(i)) {
                Ui.blocknumbers.add(i);
            }
            
            byte[] temp = new byte[4];
            System.arraycopy(rawdata, currentOffset, temp, 0, 4);
            currentOffset += 4;
            datablocksAddressesArray[i] = fourBytesToInt(temp);
        }

        for (int i = 0; i < datablocksCounter; i++)
        {
            db[i] = new Datablock(i);
            db[i].offset = datablocksAddressesArray[i];

            currentOffset = datablocksAddressesArray[i];
            byte[] temp = new byte[3];
            System.arraycopy(rawdata, currentOffset, temp, 0, 3);
            currentOffset += 3;
            db[i].startAddress = threeBytesToInt(temp);
            currentOffset++;
            System.arraycopy(rawdata, currentOffset, temp, 0, 3);
            currentOffset += 3;
            db[i].size = threeBytesToInt(temp);
            currentOffset += 12;
            db[i].checksum = ((rawdata[currentOffset] & 0xFF) << 8) | (rawdata[currentOffset + 1] & 0xFF);
            datablockInfo(db[i]);
        }
    }

    public static int fourBytesToInt(byte[] inputArray)
    {
        return  ((inputArray[3] & 0xFF) << 24) |
                ((inputArray[2] & 0xFF) << 16) |
                ((inputArray[1] & 0xFF) << 8) |
                (inputArray[0] & 0xFF);
    }

    public static int threeBytesToInt(byte[] inputArray)
    {
        return  ((inputArray[0] & 0xFF) << 16) |
                ((inputArray[1] & 0xFF) << 8) |
                (inputArray[2] & 0xFF);
    }

    public static void printFourBytes(byte[] inputArray)
    {
        System.out.format("0x%02X 0x%02X 0x%02X 0x%02X \r\n", inputArray[0], inputArray[1], inputArray[2], inputArray[3]);
    }

    public static void printArray(int[] inputArray)
    {
        for (int i = 0; i < inputArray.length; i++)
        {
            System.out.format("0x%08X ", inputArray[i]);
        }
        System.out.println();
    }

    public static void datablockInfo(Datablock db)
    {
        Ui.data.add(new TableEntry(String.format("Блок данных №%X", db.number), null, null));
        Ui.data.add(new TableEntry("Адрес записи", String.format("0x%X - 0x%X", db.startAddress, db.startAddress + db.size - 1), String.format("0x%08X", db.offset)));
        Ui.data.add(new TableEntry("Размер блока", String.format("%d байт", db.size), String.format("0x%08X", db.offset + 4)));
        Ui.data.add(new TableEntry("Контрольная сумма блока", String.format("0x%X", db.checksum), String.format("0x%08X", db.offset + 19)));
        Ui.data.add(new TableEntry("Адреса данных в SGO-файле", String.format("0x%08X - 0x%08X", db.offset + 25, db.offset + 25 + db.size - 1), null));
    }

    public static boolean saveToBin(File file, int number) throws IOException
    {
        RandomAccessFile out = new RandomAccessFile(file, "rw");
        int size = db[number].size;
        currentOffset = db[number].offset + 25;
        byte[] buffer = new byte[size];

        for (int i = 0; i < db[number].size; i++)
        {
            buffer[i] = (byte)(rawdata[currentOffset] ^ 0xFF);
            currentOffset++;
        }
        out.write(buffer);
        out.close();
        return true;
    }

    public static boolean loadBin(File file, int number) throws IOException
    {
        if (file.length() != db[number].size)
        {
            showAlert("Размер файла не соответствует длине блока!");
            return false;
        } else
        {
            RandomAccessFile in = new RandomAccessFile(file, "r");
            currentOffset = db[number].offset + 25;
            for (int i = 0; i < db[number].size; i++)
            {
                rawdata[currentOffset] = (byte)(in.readByte() ^ 0xFF);
                currentOffset++;
            }
            in.close();
            return true;
        }        
    }

    public static int calcFullChecksum() throws IOException {
        currentOffset = 0;
        int j = 0;
        int sum = 0;
        for (int i = 0; i < rawdata.length; i++)
        {
            if (i < 21 || i > 24) {
                j = rawdata[currentOffset];
                if (j < 0) {j = j & 0xFF;}
                sum += j;
            }
            currentOffset++;            
        }

        sum--;

        rawdata[0x15] = (byte)(sum & 0xFF);
        rawdata[0x16] = (byte)((sum >> 8) & 0xFF);
        rawdata[0x17] = (byte)((sum >> 16) & 0xFF);
        rawdata[0x18] = (byte)((sum >> 24) & 0xFF);

        parseData();

        return sum;
    }

    public static int calcBlockChecksum(int n) throws IOException {

        int sum = 0;

        currentOffset = db[n].offset + 25;
        int j = 0;

        for (int i = 0; i < db[n].size; i++)
        {
            j = rawdata[currentOffset];
            if (j < 0) {j = j & 0xFF;}
            sum += (j ^ 0xFF);
            currentOffset++;
        }

        sum = sum & 0xFFFF;

        rawdata[db[n].offset + 20] = (byte)(sum & 0xFF);
        rawdata[db[n].offset + 19] = (byte)((sum >> 8) & 0xFF);

        parseData();
        
        return sum;
    }

    public static int getByteFromBuffer(int address) {
        boolean isXored = false;

        int j = rawdata[address];
        if (j < 0) {j = j & 0xFF;}

        for (int i = 0; i < db.length; i++) {
            if (address >= db[i].offset + 25 && address < db[i].offset + 25 + db[i].size) {
                isXored = true;
            }
        }
        if (isXored) {
            return (j ^ 0xFF);
        } else {
            return j;
        }
    }

    public static boolean setByteToBuffer(int address, int data) {
        if (addressExist(address)) {
            boolean isXored = false;
            for (int i = 0; i < db.length; i++) {
            if (address >= db[i].offset + 25 && address < db[i].offset + 25 + db[i].size) {
                isXored = true;
            }
            }
            if (isXored) {
                rawdata[address] = (byte)(data ^ 0xFF);
            } else {
                rawdata[address] = (byte)(data);
            }
            return true;
        } else {
            return false;
        }
    }

    public static int getBufferSize() {
        return rawdata.length;
    }

    public static TableHexEntry getHexString(int address)
    {
        int len;
        if (address > rawdata.length) 
        {
            return new TableHexEntry();
        } else if (address > rawdata.length - 16)
        {
            len = rawdata.length - address;
        } else
        {
            len = 16;
        }
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < len; i++)
        {
            result.add(getByteFromBuffer(address + i));
        }        
        return new TableHexEntry(result);
    }

    public static String getStringFromHex(TableHexEntry entry)
    {
        String result = "";
        for (int i = 0; i < 16; i++)
        {
            int j = Integer.decode("0x" + entry.getCell(i).getValue().toLowerCase());
            result += (char)j;
        }
        return result;
    }

    public static boolean addressExist(int address) 
    {
        if (address >= 0 && address < rawdata.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}