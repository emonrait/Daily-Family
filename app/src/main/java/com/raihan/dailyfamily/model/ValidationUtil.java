package com.raihan.dailyfamily.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


public class ValidationUtil {

    public static String commaSeparateAmount(String str) {
        String rValue = "0.00";

        double amount = 0.00;


        try {
            //str = str.replaceAll(",", "");
            String regx = "[-+^:*#_/, %@$@!*\u09F3]";
            str = str.replaceAll(regx, "");


            if (str != null) {
                amount = Double.parseDouble(str);
            }

            Format f = DecimalFormat.getNumberInstance(new Locale("en", "IN"));
            ((DecimalFormat) f).setDecimalSeparatorAlwaysShown(true);
            ((DecimalFormat) f).setMaximumFractionDigits(2);
            ((DecimalFormat) f).setMinimumFractionDigits(2);
            ((DecimalFormat) f).setNegativePrefix("-");

            if (amount == 0.00) {
                rValue = "0.00" + " \u09F3";
            } else {
                rValue = f.format(amount) + " \u09F3";

            }

            // Log.e("rValue-->", rValue);

        } catch (Exception e) {
            rValue = "0.00";
            e.printStackTrace();
        }

        return rValue;
    }


    public static String commaSeparateMonth(String str) {
        String rValue = "";

        double amount = 0.00;


        try {
            //str = str.replaceAll(",", "");
            String regx = "[-+^:*#_/, %@$@!*\u09F3]";
            str = str.replaceAll(regx, "");


            if (str != null) {
                amount = Double.parseDouble(str);
            }

            Format f = DecimalFormat.getNumberInstance(new Locale("en", "IN"));
            ((DecimalFormat) f).setDecimalSeparatorAlwaysShown(true);
            ((DecimalFormat) f).setMaximumFractionDigits(2);
            ((DecimalFormat) f).setMinimumFractionDigits(2);

            rValue = f.format(amount) + " M";
            // Log.e("rValue-->", rValue);

        } catch (Exception e) {
            rValue = "";
            e.printStackTrace();
        }

        return rValue;
    }

    public static String replacecomma(String str) {
        String rValue = "0.0";
        try {
            //str = str.replaceAll(",", "");
            // String var10001 = "5/0,00:0.00";

            String regx = "[-+^:*#_/, %@$@!*\u09F3]";
            str = str.replaceAll(regx, "");

            //System.out.println("var9 = " + str.replaceAll("[-+^:*#_/, ]", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static double replacecommaDouble(String str) {
        double rValue = 0.00;
        try {

            String regx = "[-+^:*#_/, %@$@!*\u09F3]";
            str = str.replaceAll(regx, "");
            rValue = Double.parseDouble(str);

        } catch (Exception e) {
            e.printStackTrace();
            rValue = 0.00;
        }
        return rValue;
    }

    public static int replacePoet(String str) {
        int value = 0;
        try {
            //str = str.replaceAll(",", "");
            // String var10001 = "5/0,00:0.00";

            String regx = "[-+^:*#_/, POET%@$@!*\u09F3]";
            str = str.replaceAll(regx, "");
            value = Integer.parseInt(str);

            //System.out.println("var9 = " + str.replaceAll("[-+^:*#_/, ]", ""));
        } catch (Exception e) {
            e.printStackTrace();
            value = 0;
        }
        return value;
    }


    public static String getNullCheck(String str) {
        String rValue = "";
        if (null == str || str.isEmpty() || str == "") {
            rValue = "    ";
        } else {
            rValue = str;
        }
        return rValue;

    }

    public static boolean isNumeric(String str) {
        boolean b = false;
        try {
            if (str != "") {
                Double.parseDouble(str);
                b = true;
            }

        } catch (NumberFormatException e) {
            b = false;
        }
        return b;
    }

    public static int getNumber(String b, String l, String d) {
        int value = 0;
        try {
            if (b != "" || l != "" || d != "") {
                value = Integer.parseInt(b) + Integer.parseInt(l) + Integer.parseInt(d);

            }

        } catch (NumberFormatException e) {
            value = 0;
        }
        return value;
    }

    public static String getRetrofit_NullCheck(String s) {
        String rValue = "";
        if (null == s || s.isEmpty() || s.endsWith("null") || s == "") {
            rValue = "";
        } else {
            rValue = s;
        }
        return rValue;
    }

    public static int printNumbers(int min, int max) {
        int digit = 0;
        if (min <= max) {
//            System.out.print(num + " ");
//            printNumbers(num + 1);
            digit = min;
            Log.e("digit", String.valueOf(digit));
        }

        /*if (digit >= min && digit <= max) {
            digit = min;

            Log.e("digit456", String.valueOf(digit));
        }*/
        return digit;
    }

    public static Boolean printNumbersCheck(int acNo, int min, int max) {
        if (acNo >= min && acNo <= max) {
            return true;

        } else {
            return false;
        }

    }

    public static Boolean printCardNumbers(int cardNo) {

        if (cardNo >= 15 && cardNo <= 19) {
            return true;

        } else {
            return false;
        }


    }

    public static boolean amountCheck(String balnce, String inputbal) {
        boolean b = true;
        double oldbal = 0.0;
        double inbal = 0.0;
        try {
            if (!balnce.equals("") && !inputbal.equals("")) {
                oldbal = Double.parseDouble(balnce);
                inbal = Double.parseDouble(inputbal);
                if (oldbal < inbal) {
                    b = true;
                } else if (inbal < 1) {
                    b = true;
                } else {
                    b = false;
                }
                //b = true;
            }

        } catch (NumberFormatException e) {
            b = true;
        }
        Log.d("balnce+inputbal", balnce + inputbal);
        return b;

    }

    public static boolean amountCheck(String principal, String interest, String time) {
        boolean b = false;
        double p = 0.0;
        double i = 0.0;
        double t = 0.0;
        try {
            if (!principal.equals("") && !interest.equals("") && !time.equals("")) {
                p = Double.parseDouble(principal);
                i = Double.parseDouble(interest);
                t = Double.parseDouble(time);
                if (p > 0) {
                    b = true;
                } else if (i > 0) {
                    b = true;
                } else if (t > 0) {
                    b = true;
                }
            } else {
                b = false;
            }

        } catch (NumberFormatException e) {
            b = true;
        }
        return b;

    }


    public static String getTransactionCurrentDate() {
        String date = "";
        try {
            java.util.Date dNow = new java.util.Date();
            SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
            date = ft.format(dNow);
        } catch (Exception e) {

        }

        return date;
    }

    public static String dateFormate(Calendar datevalue) {
        String date = "";
        try {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
            date = dateFormat.format(datevalue.getTime());
        } catch (Exception e) {

        }

        return date;
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), x);
    }


    public static Double calcEmi(double p, double r, double n) {
        double R = (r / 12) / 100;
        double e = (p * R * (Math.pow((1 + R), n)) / ((Math.pow((1 + R), n)) - 1));

        return e;
    }

    public void calcEmiAllMonths(double p, double r, double n) {

        double R = (r / 12) / 100;
        double P = p;
        double e = calcEmi(P, r, n);
        double totalInt = Math.round((e * n) - p);
        double totalAmt = Math.round((e * n));
        System.out.println("***************************");
        System.out.println(" Principal-> " + P);
        System.out.println(" Rate of Interest-> " + r);
        System.out.println(" Number of Months-> " + n);

        System.out.println(" EMI -> " + Math.round(e));
        System.out.println(" Total Interest -> " + totalInt);
        System.out.println(" Total Amount -> " + totalAmt);

        System.out.println("***************************");
        /*double intPerMonth = Math.round(totalInt / n);

        for (double i = 1; i <= n; i++) {
            intPerMonth = (P * R);
            P = ((P) - ((e) - (intPerMonth)));
            System.out.println(" Month -> " + (int) i);
            System.out.println(" Interest per month -> "
                    + Math.round(intPerMonth));
            System.out.println(" Principal per month -> "
                    + Math.round((e) - intPerMonth));
            System.out.println(" Balance Principal -> " + Math.round(P));
            System.out.println("***************************");
        }*/
    }

    public static String getEmailValidate(String userId) {
        String rValue = "";

        try {

            String emailregex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{3,})$";
            Boolean b = userId.matches(emailregex);

            if (b == false) {
                // System.out.println("Email Address is Invalid");
                rValue = "1";
            } else if (b == true) {
                rValue = "0";
                //System.out.println("Email Address is Valid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return rValue;

    }

    public static LoginModel passwordValidaton(String password) {
        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        LoginModel model = new LoginModel();
        if (password.length() < 8) {
            model.setOut_code("1");
            model.setOut_message("Password must have at least 8 character");

        } else if (!specailCharPatten.matcher(password).find()) {
            model.setOut_code("1");
            model.setOut_message("Password must have at least one special character");

        } else if (!UpperCasePatten.matcher(password).find()) {
            model.setOut_code("1");
            model.setOut_message("Password must have at least one uppercase character");

        } else if (!lowerCasePatten.matcher(password).find()) {
            model.setOut_code("1");
            model.setOut_message("Password must have at least one lowercase character");

        } else if (!digitCasePatten.matcher(password).find()) {
            model.setOut_code("1");
            model.setOut_message("Password must have at least one digit character");

        } else {
            model.setOut_code("0");
            model.setOut_message("password matched success");

        }

        return model;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        String deviceId;

        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = Settings.Secure.getString(
                            context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                }
            }

        } catch (Exception ex) {
            deviceId = ex.getMessage();

        }
        return deviceId;
    }


}
