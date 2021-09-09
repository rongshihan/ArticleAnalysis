package Tools;

import java.io.*;
import java.util.*;

import com.huaban.analysis.jieba.JiebaSegmenter;

public class TFIDF {

    static HashMap<String, Double> idfMap;
    static HashSet<String> stopWordsSet;
    static double idfMedian;

    /**
     * TF-IDF分析方法
     * TF-IDF = TF * IDF
     *
     * @param content 需要分析的文本/文档内容
     * @param topN    需要返回的TF-IDF值最高的N个关键词，若超过content本身含有的词语上限数目，则默认返回全部
     * @return
     */
    public List<Keyword> analyze(String content, int topN) {
        List<Keyword> keywordList = new ArrayList<>();

        // 加载停用词表
        if (stopWordsSet == null) {
            stopWordsSet = new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/stop_words.txt"));
        }
        // 加载IDF值表
        if (idfMap == null) {
            idfMap = new HashMap<>();
            loadIDFMap(idfMap, this.getClass().getResourceAsStream("/idf_dict.txt"));
        }

        Map<String, Double> tfMap = getTF(content);
        for (String word : tfMap.keySet()) {
            // 若该词不在idf文档中，则使用平均的idf值(可能定期需要对新出现的网络词语进行纳入)
            if (idfMap.containsKey(word)) {
                keywordList.add(new Keyword(word, idfMap.get(word) * tfMap.get(word)));
            } else
                keywordList.add(new Keyword(word, idfMedian * tfMap.get(word)));
        }

        Collections.sort(keywordList, new Comparator<Keyword>() {
            @Override
            public int compare(Keyword o1, Keyword o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                return 0;
            }
        });

        if (topN >-1 && keywordList.size() > topN) {
            int num = keywordList.size() - topN;
            for (int i = 0; i < num; i++) {
                keywordList.remove(topN);
            }
        }
        return keywordList;
    }

    /**
     * 计算TF值
     * TF值计算公式
     * TF=N(i,j)/(sum(N(k,j) for all k))
     * N(i,j)表示词语Ni在该文档d（content）中出现的频率，sum(N(k,j))代表所有词语在文档d中出现的频率之和
     *
     * @param content
     * @return
     */
    private Map<String, Double> getTF(String content) {
        Map<String, Double> tfMap = new HashMap<>();
        if (content == null || content.equals(""))
            return tfMap;

        // 分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> segments = segmenter.sentenceProcess(content);

        Map<String, Integer> freqMap = new HashMap<>();

        int wordSum = 0;
        for (String segment : segments) {
            //停用词不予考虑，单字词不予考虑
            if (!stopWordsSet.contains(segment) && segment.length() > 1) {
                wordSum++;
                if (freqMap.containsKey(segment)) {
                    freqMap.put(segment, freqMap.get(segment) + 1);
                } else {
                    freqMap.put(segment, 1);
                }
            }
        }

        // TF值归一化处理
        for (String word : freqMap.keySet()) {
            tfMap.put(word, freqMap.get(word) * 0.1 / wordSum);
        }

        return tfMap;
    }

    /**
     * 默认jieba分词的停词表
     * url:https://github.com/yanyiwu/nodejieba/blob/master/dict/stop_words.utf8
     *
     * @param set
     * @param in
     */
    private void loadStopWords(Set<String> set, InputStream in) {
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufr.readLine()) != null) {
                set.add(line.trim());
            }
            try {
                bufr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * IDF值本来需要语料库来自己按照公式进行计算，不过jieba分词已经提供了一份很好的IDF字典，所以默认直接使用jieba分词的IDF字典
     * url:https://raw.githubusercontent.com/yanyiwu/nodejieba/master/dict/idf.utf8
     *
     * @param map
     * @param in
     */
    private void loadIDFMap(Map<String, Double> map, InputStream in) {
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufr.readLine()) != null) {
                String[] kv = line.trim().split(" ");
                map.put(kv[0], Double.parseDouble(kv[1]));
            }
            try {
                bufr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 计算IDF值的中位数
            List<Double> idfList = new ArrayList<>(map.values());
            Collections.sort(idfList);
            idfMedian = idfList.get(idfList.size() / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
