import java.io.File;
import java.io.FileNotFoundException;
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
        switch (transformType){
            case ""
        }
        if(transformType.equals("motionblur")){
            int blurLength = Integer.parseInt(args[3]);
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

    private class Pixel{
        private int red;
        private int green;
        private int blue;

        private Pixel(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }
}
