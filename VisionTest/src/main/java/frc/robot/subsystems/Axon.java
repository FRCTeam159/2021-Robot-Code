// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.google.gson.JsonParser;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.opencv.core.Point;

public class Axon extends SubsystemBase {

  NetworkTable table;

  public static final int LARGEST = 0;
  public static final int BEST = 1;

  /** Creates a new Axon. */
  public Axon() {
    table = NetworkTableInstance.getDefault().getTable("ML");
  }

  public Axon(double f) {
    minconf = f;
    table = NetworkTableInstance.getDefault().getTable("ML");
  }

  ArrayList<TargetData> targets = new ArrayList<TargetData>();
  double fps = 0;
  double minconf = 0.5;
  int mode = BEST;
  String type = null;
  ArrayList<String> types = new ArrayList<String>();
  TargetData target;

  void setMinConf(double f) {
    minconf = f;
  }

  void setMode(int i) {
    if (mode < 2)
      mode = i;
  }

  int getMode() {
    return mode;
  }

  void setType(String t) {
    type = t;
  }

  // get target data from network tables (supplied by RaspberryPi/corel hw running Axon)
  public int getTargets() {
    fps = table.getEntry("fps").getDouble(0);
    String detections = table.getEntry("detections").getString("");
    targets.clear();
    types.clear();

    int num = 0;
    double conf = 0;
    double x1 = 0;
    double x2 = 0;
    double y1 = 0;
    double y2 = 0;
    try {
      if (detections.length() > 10) {
        JsonElement elements = JsonParser.parseString(detections);
        JsonArray array = elements.getAsJsonArray();
        num = array.size();
        for (int i = 0; i < num; i++) {
          JsonElement elem = array.get(i);
          JsonObject jsonObject = elem.getAsJsonObject();
          String label = jsonObject.get("label").toString();
          String cstr = jsonObject.get("confidence").toString();
          JsonObject b = jsonObject.get("box").getAsJsonObject();
          x1 = Double.parseDouble(b.get("xmin").toString());
          x2 = Double.parseDouble(b.get("xmax").toString());
          y1 = Double.parseDouble(b.get("ymin").toString());
          y2 = Double.parseDouble(b.get("ymax").toString());
          conf = Double.parseDouble(cstr);

          if (conf > minconf) {
            TargetData t = new TargetData(label, conf, x1, x2, y1, y2);
            // System.out.println(t);

            targets.add(t);
            if (!types.contains(label))
              types.add(label);
          }
        }
      }
    } catch (Exception e) {
      String str = "Exception:" + e;
      System.out.println(str);
      targets.clear();
      return 0;
    }
    return num;
  }

  public double getFps() {
    return fps;
  }

  public int numTargets() {
    return targets.size();
  }

  // return largest target
  public TargetData largest(String type) {
    int n = numTargets();
    if (n == 0)
      return null;
    double max_area = 0;
    TargetData target = null;
    for (int i = 0; i < n; i++) {
      if (type == null || targets.get(i).name().equals(type)) {
        double area = targets.get(i).area();
        if (area > max_area) {
          target = targets.get(i);
          max_area = area;
        }
      }
    }
    return target;
  }

  // return target with highest confidence;
  public TargetData best(String type) {
    int n = numTargets();
    if (n == 0)
      return null;
    double max_conf = 0;
    TargetData target = null;
    for (int i = 0; i < n; i++) {
      if (type == null || targets.get(i).name().equals(type)) {
        double conf = targets.get(i).confidence;
        if (conf > max_conf) {
          target = targets.get(i);
          max_conf = conf;
        }
      }
    }
    return target;
  }

  public void process() {
    // This method will be called once per scheduler
    getTargets();
  }

  public TargetData getTarget(){
    String seltype = null;

    target = null;
    if (targets.size() > 0) {
      if (type == null || !types.contains(type))
        seltype = types.get(0);
      else 
        seltype=type;
      target = (mode == BEST ? best(seltype) : largest(seltype));
    }
    return target;
  }

  public class TargetData {
    public String type;
    public Rectangle rect;
    public double confidence = 0;

    public TargetData(String s, double conf, double x1, double x2, double y1, double y2) {
      type = new String(s);
      confidence = conf;
      rect = new Rectangle(x1, x2, y1, y2);
    }

    public String name() {
      return type;
    }

    public double conf() {
      return confidence;
    }

    public double area() {
      return rect.area();
    }

    public Point center() {
      return rect.center();
    }

    public Point ul() {
      return rect.ul;
    }

    public Point lr() {
      return rect.lr;
    }

    public String toString() {
      return new String("type:" + type + " conf:" + confidence + " " + rect);
    }
  }

  public class Rectangle {
    public Point ul = new Point();
    public Point lr = new Point();

    public Rectangle(double xmin, double xmax, double ymin, double ymax) {
      ul.x = xmin;
      ul.y = ymin;
      lr.x = xmax;
      lr.y = ymax;
    }

    public double width() {
      return lr.x - ul.x;
    }

    public double height() {
      return lr.y - ul.y;
    }

    public double area() {
      return width() * height();
    }

    public Point center() {
      return new Point(ul.x + 0.5 * width(), ul.y + 0.5 * height());
    }

    public String toString() {
      return new String("Rect(" + ul.x + "," + lr.x + "," + ul.y + "," + lr.y + ")");
    }
  }
}