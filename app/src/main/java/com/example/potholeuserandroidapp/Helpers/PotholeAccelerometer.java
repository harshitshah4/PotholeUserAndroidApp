package com.example.potholeuserandroidapp.Helpers;


import java.util.LinkedList;
import java.util.Queue;

public class PotholeAccelerometer {

    static int n=0;
    static double running_avg = 5.81F;
    final static double g=9.81F;
    final static double threshold=0.4*g;
    static Queue<Double> q = new LinkedList<>();

    public static int calc(double x, double y, double z)
    {
        double rms = Math.sqrt((x*x+y*y+z*z)/3);
        q.add(rms);
        double sum=0;
        for(Double item : q){
            sum=sum+(item-running_avg)*(item-running_avg);
        }
        double sd= Math.sqrt(sum/q.size());
        if(sd>threshold)
        {
            q.clear();
            return 1;

        }
        else
        {
            double temp=q.remove();
            running_avg=(running_avg*n+temp)/(n+1);
            n+=1;

        }
        return 0;

    }
}
