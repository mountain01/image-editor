import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Math;
import java.lang.StringBuffer;
import java.lang.StringBuilder;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class ImageEditor{
    private static int width;
    private static int height;
    private static int maxColor;
    private static Pixel[][] picture;

    public static void main(String[] args){
        String inputFileName = args[0];
        String outputFileName = args[1];
        String transformType = args[2];
        ImageEditor ie = new ImageEditor();
        ie.readFile(inputFileName);
        ie.doTransformation();
        ie.writeImage(outputFileName);
    }

    public void writeImage(String output){
        PrintWriter out = new PrintWriter(new File(output));
        StringBuilder output = new StringBuilder();
        output.append("P3\n"+width + " " + height + " " + maxColor + "\n");
        for(Pixel[] row : picture){
            for(Pixel p : row){
                output.append(p.red +" "+p.green+" "+p.blue+"\n");
            }
        }
        out.write(output.toString());
        out.close();
    }

    public void doTransformation(){
        for(int i = heigth - 1;i>=0;i--){
            for(int k = 0, k < width; k++){
                Pixel pixel = picture[i][k];
                switch (transformType){
                    case "invert":
                        pixel.invert();
                        break;
                    case "grayscale":
                        pixel.grayscale();
                        break;
                    case "emboss":
                        if(i<1 || k <1){
                            pixel.embose(128);
                        } else {
                            pixel.embose(getEmbossValue(pixel, picture[i - 1][k - 1]);
                        }
                        break;
                    case "motionblur":
                        int blurLength = Integer.parseInt(args[3]);
                        pixel.blur(blur(getBlurPixels(i,k,blurLength)));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private ArrayList<Pixel> getBlurPixels (int i, int k, int blur){
        ArrayList<Pixel> values;
        int blurBoundary = k + blur -1 >= width ? width - k : blur;
        for(int j = 0: j < blurBoundary; j++){
            values.add(picture[i][k+blurBoundary]);
        }

        return values;
    }

    private Pixel blur(ArrayList<Pixel> values){
        int reds = 0;
        int greens =0;
        int blues = 0;
        for(Pixel p:values){
            reds += p.red;
            greens += p.green;
            blues += p.blue;
        }

        return new Pixel(reds/values.size(),greens/values.size(),blues/values.size());
    }

    public void readFile(String inputFile){
        try {
            Scanner in = new Scanner(new File(inputFile));
            in.next();
            width = in.nextInt();
            height = in.nextInt();
            maxColor = in.nextInt();
            picture = new Pixel[height][width];
            for(int i = 0;i<height;i++){
                for(int k = 0;k<width;k++){
                    picture[i][k] = new Pixel(in.nextInt(),in.nextInt(),in.nextInt());
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getEmbossValue(Pixel Pixel1,Pixel Pixel2){
        int redDiff = Pixel1.red - Pixel2.red;
        int greenDiff =Pixel1.green - Pixel2.green;
        int blueDiff = Pixel1.blue - Pixel2.blue;
        int maxDiff = Math.abs(redDiff) > Math.abs(greenDiff) ? redDiff : greenDiff;
        maxDiff = Math.abs(maxDiff) > Math.abs(blueDiff) ? maxDiff : blueDiff;
        maxDiff += 128;
        if (maxDiff > 255){
            maxDiff = 255;
        } else if(maxDiff<0){
            maxDiff = 0;
        }
    }

    private class Pixel{
        private int red;
        private int green;
        private int blue;

        private Pixel(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        private void invert(){
            this.red = 255 - this.red;
            this.green = 255 - this.green;
            this.blue = 255 - this.blue;
        }

        private void grayscale(){
            int avg = (this.red + this.green + this.blue)/3);
            this.red = this.green = this.blue = avg;
        }

        private void embose(int value){
            this.red = this.blue = this.green = value;
        }

        private void blur(Pixel pixel){
            this.red = pixel.red;
            this.green = pixel.green;
            this.blue = pixel.blue;
        }
    }
}
