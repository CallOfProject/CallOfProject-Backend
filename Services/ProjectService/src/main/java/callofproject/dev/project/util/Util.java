package callofproject.dev.project.util;

import callofproject.dev.library.exception.service.DataServiceException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class Util
{
    private Util()
    {

    }

    public static byte[] compressImageToJPEG(MultipartFile file)
    {
        try
        {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // Create a BufferedImage with RGB color space for compatibility with JPEG
            BufferedImage rgbImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.createGraphics().drawImage(originalImage, 0, 0, null);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Create JPEG writer with specified quality
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f); // You can adjust the compression quality here

            // Write the compressed image to the output stream
            writer.setOutput(new MemoryCacheImageOutputStream(outputStream));
            writer.write(null, new IIOImage(rgbImage, null, null), param);

            // Encode the output stream to base64 or return byte array
            byte[] compressedBytes = outputStream.toByteArray();

            // Close writer and output stream
            writer.dispose();
            outputStream.close();

            return compressedBytes;
        } catch (IOException e)
        {
            throw new DataServiceException("Error occurred while compressing image!", e);
        }
    }
}
