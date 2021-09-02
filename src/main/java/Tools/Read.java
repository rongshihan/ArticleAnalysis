package Tools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Read {
    /**
     * 读取text（dic）文件
     *
     * @param url
     * @return
     */
    public static String readTxt(String url) {
        StringBuilder txt = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(url);
            // 防止路径乱码，如果utf-8乱码，改GBK，eclipse里创建的txt，用UTF-8，在电脑上自己创建的txt，用GBK
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                txt.append(line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txt.toString();
    }

    /**
     * 读取pdf文件
     *
     * @param url
     * @return
     */
    public static String readPdf(String url) {
        String content = null;

        try {
            PDDocument doc = PDDocument.load(new File(url));
            PDFTextStripper textStripper = new PDFTextStripper();
            content = textStripper.getText(doc);

            // 去掉空格、回车、制表符、换行符
            if (content!=null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(content);
                content = m.replaceAll("");
                content = content.replaceAll("[\\p{P}]","");
            }
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
