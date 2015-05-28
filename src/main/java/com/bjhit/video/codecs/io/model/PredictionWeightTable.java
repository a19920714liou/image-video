package com.bjhit.video.codecs.io.model;

/**
 * 预测权重表
 * @description
 * @project bjhit-video
 * @author guanxianchun
 * @Create 2015-2-11 下午3:36:21
 * @version 1.0
 */
public class PredictionWeightTable {
    public int luma_log2_weight_denom;
    public int chroma_log2_weight_denom;
    
    public int[][] luma_weight = new int[2][];
    public int[][][] chroma_weight = new int[2][][];
    
    public int[][] luma_offset = new int[2][];
    public int[][][] chroma_offset = new int[2][][];
    
}
