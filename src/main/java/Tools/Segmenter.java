package Tools;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Segmenter {
    /**
     * 读取dic文件
     *
     * @param in
     * @return
     */
    private static ArrayList<String> getStopWords(InputStream in) {
        // 使用一个字符串集合来存储文本中的路径，也可用String[]数组
        ArrayList<String> list = new ArrayList<>();
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufr.readLine()) != null) {
                // 如果txt文件里的路径不包含---字符串，这里是对里面的内容进行一个筛选
                if (line.lastIndexOf("---") < 0) {
                    list.add(line);
                }
                try {
                    bufr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 分词
     *
     * @param str
     * @return
     */
    private List<Term> getTerms(String str) {
        // 关闭名字识别
        // MyStaticValue.isNameRecognition = false;
        // 配置自定义词典的位置。
        // MyStaticValue.ENV.put(DicLibrary.DEFAULT,"");

        // 去停用词
        List<String> stopWords = getStopWords(this.getClass().getResourceAsStream("/stop_words.txt"));
        StopRecognition filter = new StopRecognition();
        filter.insertStopWords(stopWords);

        // 分词结果的一个封装，主要是一个List<Term>的terms
        Result result = ToAnalysis.parse(str).recognition(filter);

        // Result result = ToAnalysis.parse(str);

        return result.getTerms();
    }

    /**
     * 拿到分词结果
     *
     * @param url
     * @return
     */
    public ArrayList<String> getWords(String url) {
        // 读txt文件
        String str = Read.readPdf(url);
        // 词保存在words
        ArrayList<String> words = new ArrayList<>();

        List<Term> terms = getTerms(str);
        for (Term term : terms) {
            String word = term.getName();
            words.add(word);
        }
        return words;
    }

    /**
     * 计算词频
     *
     * @param url
     * @return
     */
    public HashMap<String, Integer> getWordFreq(String url) {
        HashMap<String, Integer> hashMap = new HashMap<>();

        for (String word : getWords(url)) {
            if (!word.equals("\n") && !word.equals(" ") && !word.equals("\r")) {
                Integer count = hashMap.get(word);
                hashMap.put(word, (count == null) ? 1 : count + 1);
            }
        }

        return hashMap;
    }
}
