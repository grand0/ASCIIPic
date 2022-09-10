import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("You need to provide path to image!");
            return;
        }

        // read image
        BufferedImage image = ImageIO.read(new File(args[0]));
        // make it grayscale by drawing it on "gray only" image
        BufferedImage grayscale = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayscale.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        // symbols to draw
        char[] luminance = "  .,:;ox#%@".toCharArray();
        // block size (bigger block == smaller image)
        // best to keep ratio at around 1:2 - 1:1.5 (need more experiments)
        int blockWidth = 7;
        int blockHeight = 16;

        // go through each block
        for (int y = 0; y < grayscale.getHeight(); y += blockHeight) {
            for (int x = 0; x < grayscale.getWidth(); x += blockWidth) {
                // determine the actual size of block
                int width = Math.min(blockWidth, grayscale.getWidth()-x);
                int height = Math.min(blockHeight, grayscale.getHeight()-y);

                // get block
                BufferedImage block = grayscale.getSubimage(x, y, width, height);

                // calculate average gray
                int blockGrayValue = 0;
                for (int blockY = 0; blockY < height; blockY++) {
                    for (int blockX = 0; blockX < width; blockX++) {
                        int pixel = block.getRGB(blockX, blockY);
                        String hexRgb = Integer.toHexString(pixel);
                        // since image is already gray, R, G and B values
                        // are equal => we need only one value (2 digits)
                        String hexGray = hexRgb.substring(hexRgb.length() - 2);
                        int gray = Integer.parseInt(hexGray, 16);
                        blockGrayValue += gray;
                    }
                }
                blockGrayValue /= width * height;

                // determine which character to draw based on gray value
                char c = luminance[(int) ((blockGrayValue / 255.0) * luminance.length)];

                // print that character
                System.out.print(c);
            }
            // start a new line
            System.out.println();
        }
    }
}
