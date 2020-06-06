package ledServer;
import java.awt.image.BufferedImage;
import java.awt.image.*;

public class FastRGB
{

    private int width;
    private int[] pixels;

    public FastRGB(BufferedImage image)
    {
        pixels = ((DataBufferInt)image.getData().getDataBuffer()).getData();
        width = image.getWidth();
    }

    public int getRGB(int x, int y)
    {
        int pos = (y * width) + x;                  
        return pixels[pos];
    }
}