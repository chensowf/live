package cn.mz.live.utils;


import java.security.MessageDigest;

public class MD5 {

    public static String getMd5(String str){
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(str.getBytes());
            byte[] result =mdInst.digest();
            return bytesToHexString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
