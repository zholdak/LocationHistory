package com.zholdak.locationshistory.location;

import java.math.BigDecimal;

/**
 * @author Aleksey Zholdak (aleksey@zholdak.com) 2018-06-07 09:37
 */
public class Distance {

  /**
   * Calculate distance between two points in latitude and longitude. If you are interested in height, use this
   * method instead: {@link #distance(double, double, double, double, double, double)}.
   *
   * @param startLat Start point latitude
   * @param startLon Start point longitude
   * @param endLat End point latitude
   * @param endLon End point longitude
   * @return Distance in Meters
   */
  public static double distance(double startLat, double startLon, double endLat, double endLon) {
    return distance(startLat, startLon, endLat, endLon, 0.0, 0.0);
  }

  /**
   * Calculate distance between two points in latitude and longitude. If you are interested in height, use this
   * method instead: {@link #distance(double, double, double, double, double, double)}.
   *
   * @param startLat Start point latitude
   * @param startLon Start point longitude
   * @param endLat End point latitude
   * @param endLon End point longitude
   * @return Distance in Meters
   */
  public static BigDecimal distance(BigDecimal startLat, BigDecimal startLon, BigDecimal endLat, BigDecimal endLon) {
    return new BigDecimal(
        distance(startLat.doubleValue(), startLon.doubleValue(), endLat.doubleValue(), endLon.doubleValue(), 0.0, 0.0));
  }

  /**
   * Calculate distance between two points in latitude and longitude taking into account height difference. If you
   * are not interested in height difference pass 0.0. Uses Haversine method as its base.
   *
   * @param startLat Start point latitude
   * @param startLon Start point longitude
   * @param endLat End point latitude
   * @param endLon End point longitude
   * @param startEl Start altitude in meters
   * @param endEl End altitude in meters
   * @return Distance in Meters
   */
  public static BigDecimal distance(BigDecimal startLat, BigDecimal startLon, BigDecimal endLat, BigDecimal endLon,
      BigDecimal startEl, BigDecimal endEl) {
    return new BigDecimal(
        distance(startLat.doubleValue(), startLon.doubleValue(), endLat.doubleValue(), endLon.doubleValue(),
            startEl.doubleValue(), endEl.doubleValue()));
  }

  /**
   * Calculate distance between two points in latitude and longitude taking into account height difference. If you
   * are not interested in height difference pass 0.0. Uses Haversine method as its base.
   *
   * @param startLat Start point latitude
   * @param startLon Start point longitude
   * @param endLat End point latitude
   * @param endLon End point longitude
   * @param startEl Start altitude in meters
   * @param endEl End altitude in meters
   * @return Distance in Meters
   */
  public static double distance(double startLat, double startLon, double endLat, double endLon, double startEl,
      double endEl) {

    final int R = 6371; // Radius of the earth

    double latDistance = Math.toRadians(endLat - startLat);
    double lonDistance = Math.toRadians(endLon - startLon);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(startLat)) * Math
        .cos(Math.toRadians(endLat)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = startEl - endEl;

    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
  }
}
