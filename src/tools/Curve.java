package tools;

public class Curve{
    //曲线相关参数
    public int attackSampleNum;
    public int attackSampleStart;
    public int attackCurveStart;
    public int attackCurveNum;

    //DTW、StaticAlign、POC相关参数
    public int baseCurveIndex;

    //StaticAlign相关参数
    public int maxWindow;

    //PCA、LDA、KPCA相关参数
    public int reduceDimension;
}
