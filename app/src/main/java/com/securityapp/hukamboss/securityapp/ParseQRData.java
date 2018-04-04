package com.securityapp.hukamboss.securityapp;

/**
 * Created by Bhupa on 01/03/18.
 */

public class ParseQRData {

    public static void main(String arg[]){
        String output = "BEGIN:VCARD\nVERSION:3.0\nN:Labhe;Mahesh\nFN:Mahesh Labhe\nORG:Health Plus\nTITLE:PhysioTherapist\nADR:;;Shastri Nagar;Dombivli (West);;;India\nTEL;WORK;VOICE:\nTEL;CELL:9821684121\nTEL;FAX:\nEMAIL;WORK;INTERNET:\nURL:\nBDAY:1\nEND:VCARD";
        String[] result = output.split("\n");
        System.out.println(result.length);
        String ID = "";

        for (int i = 0; i < result.length; i++) {
            System.out.println();
            String[] resultVal = result[i].split(":");

            if(resultVal[0].equalsIgnoreCase("BDAY")) {
                ID = resultVal[1];
            }
            for (int j = 0; j < resultVal.length; j++) {
                System.out.print(resultVal[j]);
                if(j == 0) {
                    System.out.print(" : ");
                }
            }
        }
        System.out.println(ID);
    }
}
