package com.toomtoome.fire.Utils;

/**
 * Created by moogunjung on 12/18/17.
 */

public class TimeConverter {

//    public static String convertLongDateToAgoString (Double createdDate, Double timeNow){
//        Double timeElapsed = timeNow - createdDate;
//
//        // For logging in Android for testing purposes
//        /*
//        Date dateCreatedFriendly = new Date(createdDate);
//        Log.d("MicroR", "dateCreatedFriendly: " + dateCreatedFriendly.toString());
//        Log.d("MicroR", "timeNow: " + timeNow.toString());
//        Log.d("MicroR", "timeElapsed: " + timeElapsed.toString());*/
//
//        // Lengths of respective time durations in Long format.
//        Double oneMin = 60000L;
//        Double oneHour = 3600000L;
//        Double oneDay = 86400000L;
//        Double oneWeek = 604800000L;
//
//        String finalString = "0sec";
//        String unit;
//
//        if (timeElapsed < oneMin){
//            // Convert milliseconds to seconds.
//            double seconds = (double) ((timeElapsed / 1000));
//            // Round up
//            seconds = Math.round(seconds);
//            // Generate the friendly unit of the ago time
//            if (seconds == 1) {
//                unit = "sec";
//            } else {
//                unit = "secs";
//            }
//            finalString = String.format("%.0f", seconds) + unit;
//        } else if (timeElapsed < oneHour) {
//            double minutes = (double) ((timeElapsed / 1000) / 60);
//            minutes = Math.round(minutes);
//            if (minutes == 1) {
//                unit = "min";
//            } else {
//                unit = "mins";
//            }
//            finalString = String.format("%.0f", minutes) + unit;
//        } else if (timeElapsed < oneDay) {
//            double hours   = (double) ((timeElapsed / 1000) / 60 / 60);
//            hours = Math.round(hours);
//            if (hours == 1) {
//                unit = "hr";
//            } else {
//                unit = "hrs";
//            }
//            finalString = String.format("%.0f", hours) + unit;
//        } else if (timeElapsed < oneWeek) {
//            double days   = (double) ((timeElapsed / 1000) / 60 / 60 / 24);
//            days = Math.round(days);
//            if (days == 1) {
//                unit = "day";
//            } else {
//                unit = "days";
//            }
//            finalString = String.format("%.0f", days) + unit;
//        } else if (timeElapsed > oneWeek) {
//            double weeks = (double) ((timeElapsed / 1000) / 60 / 60 / 24 / 7);
//            weeks = Math.round(weeks);
//            if (weeks == 1) {
//                unit = "week";
//            } else {
//                unit = "weeks";
//            }
//            finalString = String.format("%.0f", weeks) + unit;
//        }
//        return finalString;
//    }

}
