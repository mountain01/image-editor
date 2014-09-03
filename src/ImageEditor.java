import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
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

    }

    public void doTransformation(){
        for(int i = heigth - 1;i>=0;i--){
            for(int k = width - 1; k >=0;k--){
                switch (transformType){
                    case "invert":
                        picture[i][k].invert();
                        break;
                    case "grayscale":
                        picture[i][k].grayscale();
                        break;
                    case "emboss":
                        if(i<0 || k <0){
                            picture[i][k].embose(128);
                        } else {
                            picture[i][k].embose(getEmbossValue(picture[i][k],picture[i-1][k-1]);
                        }
                        break;
                    case "motionblur":
                        int blurLength = Integer.parseInt(args[3]);
                        break;
                    default:
                        break;
                }
            }
        }
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

    private int getEmbossValue(Pixel1,Pixel2){
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
    }
}
