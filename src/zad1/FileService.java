package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;


public class FileService extends SimpleFileVisitor<Path> {
    FileChannel resultFileChannel;

    public FileService(Path fileChannelPath) throws IOException {
        resultFileChannel = FileChannel.open(fileChannelPath, StandardOpenOption.WRITE,StandardOpenOption.CREATE);
    }

    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        long fileSize = basicFileAttributes.size();
        //System.out.println("File " + path + " is " + fileSize + " bytes.");
        convertToUTF(FileChannel.open(path), fileSize);

        return FileVisitResult.CONTINUE;
    }

    private void convertToUTF(FileChannel inFileChannel, long fileSize) throws IOException {
        int byteBufferSize = (int)fileSize+1;
        ByteBuffer rawByteBuffer = ByteBuffer.allocate(byteBufferSize);
        rawByteBuffer.clear();

        inFileChannel.read(rawByteBuffer);
        rawByteBuffer.flip(); //flip - "obroc" buffer z odczytu do zapisu

        CharBuffer decoderCharBuffer = Charset.forName("Cp1250").decode(rawByteBuffer);
        ByteBuffer encoderByteBuffer = StandardCharsets.UTF_8.encode(decoderCharBuffer);

        while (encoderByteBuffer.hasRemaining()){
            resultFileChannel.write(encoderByteBuffer);
        }
    }
}
