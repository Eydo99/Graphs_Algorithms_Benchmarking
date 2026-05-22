package graphs.benchmark;


import java.util.Arrays;

public class Stats {
    public static double mean(long[] times)
    {
        long sum=0;
        for (long time : times) {
            sum += time;
        }
        return (double) sum /times.length;
    }

    public static double median(long[] times)
    {
        long[] sortedArr= Arrays.copyOf(times,times.length);
        Arrays.sort(sortedArr);
        int length=sortedArr.length;
        if(length %2 ==1)
        {
            return sortedArr[length/2];
        }
        else
        {
            return (double) (sortedArr[length/2]+sortedArr[(length/2)-1])/2;
        }
    }

    public static double standardDeviation(long[] times)
    {
        double mean=mean(times);

        double sum=0;
        for (long time : times) {
            sum += Math.pow(time - mean, 2);
        }
        return Math.sqrt(sum /times.length);
    }
}
