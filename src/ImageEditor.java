import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Math;
import java.lang.StringBuffer;
import java.lang.StringBuilder;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.IllegalFormatFlagsException;
import java.util.Map;
import java.util.Scanner;

public class ImageEditor{
    private int width;
    private int height;
    private int maxColor;
    private Pixel[][] picture;

    public static void main(String[] args){
        try{
            String inputFileName = args[0];
            String outputFileName = args[1];
            String transformType = args[2];
            int blurSize = args.length == 4 ? Integer.parseInt(args[3]): 0;
            ImageEditor ie = new ImageEditor();
            ie.readFile(inputFileName);
            ie.doTransformation(transformType, blurSize);
            ie.writeImage(outputFileName);
        } catch (IndexOutOfBoundsException e){
            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
        } catch (IllegalArgumentException e){
            System.out.println("Usage: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
        }
    }

    private int getNextInt(Scanner in){
        skipComments(in);
        return in.nextInt();
    }

    private void skipComments(Scanner in){
           while(in.hasNext("#[^\n]*")){
                in.nextLine();
           }
    }

    public void writeImage(String filename){
        PrintWriter out;
        try {
            out = new PrintWriter(new File(filename));
            StringBuilder output = new StringBuilder();
            output.append("P3\n").append(width).append(" ").append(height).append(" ").append(maxColor).append("\n");
            for(Pixel[] row : picture){
                for(Pixel p : row){
                    output.append(p.red).append(" ").append(p.green).append(" ").append(p.blue).append("\n");
                }
            }
            out.write(output.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doTransformation(String transformType, int blursize){
        for(int i = height - 1;i>=0;i--){
            for(int k = 0; k < width; k++){
                Pixel pixel = picture[i][k];
                if (transformType.equals("invert")) {
                    pixel.invert();

                } else if (transformType.equals("grayscale")) {
                    pixel.grayscale();

                } else if (transformType.equals("emboss")) {
                    if (i < 1 || k < 1) {
                        pixel.embose(128);
                    } else {
                        pixel.embose(getEmbossValue(pixel, picture[i - 1][k - 1]));
                    }

                } else if (transformType.equals("motionblur")) {
                    if(blursize == 0){
                        throw new IllegalArgumentException();
                    }
                    pixel.blur(blur(getBlurPixels(i, k, blursize, pixel)));

                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private ArrayList<Pixel> getBlurPixels (int i, int k, int blur, Pixel pixel){
        ArrayList<Pixel> values = new ArrayList<Pixel>();
        values.add(pixel);
        int blurBoundary = (k + blur >= width) ? width - k : blur;
        for(int j = 0; j < blurBoundary; j++){
            values.add(picture[i][k+j]);
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
            skipComments(in);
            in.next();
            skipComments(in);
            width = getNextInt(in);
            height = getNextInt(in);
            maxColor = getNextInt(in);
            picture = new Pixel[height][width];
            for(int i = 0;i<height;i++){
                for(int k = 0;k<width;k++){
                    picture[i][k] = new Pixel(getNextInt(in),getNextInt(in),getNextInt(in));
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
        return maxDiff;
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
            int avg = (this.red + this.green + this.blue)/3;
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
